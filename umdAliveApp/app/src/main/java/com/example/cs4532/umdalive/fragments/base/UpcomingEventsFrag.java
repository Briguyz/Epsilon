package com.example.cs4532.umdalive.fragments.base;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * @author Henry Trinh
 *
 * 12/3/2020
 * Version 1.0
 *
 * Class that creates the upcoming Events Page
 */
public class UpcomingEventsFrag extends Fragment implements View.OnClickListener {

    //View
    View view;

    //Layout Components
    private LinearLayout allEventsLinearLayout;
    private ArrayList<UpcomingEventsMaker> tempUpcomingEventArray;
    private Date strDate;


    /**
     * Creates the page whenever All Events is clicked in the app
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view The view of All Events
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Create View
        view =  inflater.inflate(R.layout.upcoming_events_layout, container, false);

        //Get Layout Components
        getLayoutComponents();

        //Use Volley Singleton to Update Page UI
        RestSingleton restSingleton = RestSingleton.getInstance(view.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, restSingleton.getUrl() + "getAllEvents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            updateUI(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error connecting", String.valueOf(error));
            }
        });
        restSingleton.addToRequestQueue(stringRequest);

        //Return the View
        return view;
    }

    /**
     * Allows a user to click on a event name to go to that event's page
     * @param clickedView The event name clicked
     * @return nothing
     */
    @Override
    public void onClick(View clickedView) {
        String TAG = (String) clickedView.getTag();

        EventFrag frag = new EventFrag();
        Bundle data = new Bundle();
        data.putString("eventID", TAG);
        frag.setArguments(data);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();

    }

    /**
     * Retrieves all layout components from upcomingevents_layout.xml
     * @return nothing
     */
    private void getLayoutComponents () {
        allEventsLinearLayout = (LinearLayout) view.findViewById(R.id.UpcomingEventsLayout);
    }

    /**
     * Adds event names stored in the database
     * @param res The response from the database
     * @return nothing
     * @exception JSONException Error in JSON processing
     * @see JSONException
     */
    private void updateUI(JSONObject res) throws JSONException {
        getActivity().findViewById(R.id.PageLoading).setVisibility(View.GONE);
        JSONArray allEvents = res.getJSONArray("events");
        tempUpcomingEventArray = new ArrayList<>();

        for (int i=0;i<allEvents.length();i++) {
            String name = allEvents.getJSONObject(i).getString("name");
            String date = allEvents.getJSONObject(i).getString("date");
            String time = allEvents.getJSONObject(i).getString("time");
            String id = allEvents.getJSONObject(i).getString("_id").toString();
            Log.d("comment", name);
            if((date != null) && (isValidDate(date))) {
                UpcomingEventsMaker singleEvent = new UpcomingEventsMaker(name, date, time, id);
                tempUpcomingEventArray.add(singleEvent);
                Log.d("size", String.valueOf(tempUpcomingEventArray.size()));
                Collections.sort(tempUpcomingEventArray);
            }

        }
        //This will take the temparray and move it to the view
        Collections.sort(tempUpcomingEventArray);
        Log.d("size", String.valueOf(tempUpcomingEventArray.size()));
        for(int j=0;j<tempUpcomingEventArray.size();j++) {
            UpcomingEventsMaker tempItem = tempUpcomingEventArray.get(j);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            try {
                strDate = sdf.parse(tempItem.getEventDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(System.currentTimeMillis() < strDate.getTime()) {
                TextView eventName = new TextView(view.getContext());
                eventName.setText(tempItem.getEventName()+ ":    " +tempItem.getEventDate());
                eventName.setTextSize(24);
                eventName.setOnClickListener(this);
                eventName.setTag(tempItem.getEventId());
                allEventsLinearLayout.addView(eventName);
            }


        }
    }

    /**
     *
     * @param str
     * @return boolean
     * A function to check if the date is a valid date
     */
    private boolean isValidDate(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try{
            formatter.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}

