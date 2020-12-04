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

/**
 * @author Jacob Willmsen, Brian, Josh Tindell, Henry Trinh
 *
 * 12/3/2020
 * Version 1.0
 *
 * Class that creates the All Events Page
 */
public class AllEventsFrag extends Fragment implements View.OnClickListener {

    //View
    View view;

    //Layout Components
    private LinearLayout allEventsLinearLayout;

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
        view =  inflater.inflate(R.layout.all_event_layout, container, false);

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
     * Allows a user to click on a club name to go to that club's page
     * @param clickedView The club name clicked
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
     * Retrieves all layout components from all_club_layout.xml
     * @return nothing
     */
    private void getLayoutComponents () {
        allEventsLinearLayout = (LinearLayout) view.findViewById(R.id.AllEventsLayout);
    }

    /**
     * Adds club names stored in the database
     * @param res The response from the database
     * @return nothing
     * @exception JSONException Error in JSON processing
     * @see JSONException
     */
    private void updateUI(JSONObject res) throws JSONException {
        getActivity().findViewById(R.id.PageLoading).setVisibility(View.GONE);
        JSONArray allEvents = res.getJSONArray("events");
        for (int i=0;i<allEvents.length();i++) {
            String name = allEvents.getJSONObject(i).getString("name");
            String time = allEvents.getJSONObject(i).getString("date");
            String id = allEvents.getJSONObject(i).getString("_id").toString();
            TextView eventName = new TextView(view.getContext());
            eventName.setText(name + ":    " + time);
            eventName.setTextSize(24);
            eventName.setOnClickListener(this);
            eventName.setTag(id);
            allEventsLinearLayout.addView(eventName);
        }
    }

}

