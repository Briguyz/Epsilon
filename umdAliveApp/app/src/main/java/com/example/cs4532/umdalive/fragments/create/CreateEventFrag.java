package com.example.cs4532.umdalive.fragments.create;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.cs4532.umdalive.MainActivity;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.base.CommentsViewFrag;
import com.example.cs4532.umdalive.fragments.base.EventFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Josh on 4/25/2018.
 */

/**
 * @author Josh Senst
 * <p>
 * 4/26/2018
 * <p>
 * Class that allows for the editing of events on the edit events page
 */

public class CreateEventFrag extends Fragment implements View.OnClickListener {
    //View
    View view;

    //Layout Components
    private EditText EventName;
    private EditText EventDescription;
    private EditText EventTime;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private EditText EventDate;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private EditText EventImage;
    private Button CreateEventButton;
    private JSONObject clubData;

    /**
     * Creates the page for Editing Events when the edit events button is pressed
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view The View of the create events page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Create View
        view = inflater.inflate(R.layout.create_event_layout, container, false);

        //Set Event Data
        try {
            clubData = new JSONObject();
            clubData.put("clubID", getArguments().getString("clubID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Get Layout Components
        getLayoutComponents();

        //Return View
        return view;
    }

    /**
     * Gets the layout components from edit_event_layout.xml
     *
     * @return nothing
     */
    private void getLayoutComponents() {
        EventName = view.findViewById(R.id.EventName);
        EventDescription = view.findViewById(R.id.EventDescription);
        EventTime = view.findViewById(R.id.EventTime);
        EventDate = view.findViewById(R.id.EventDate);
        EventImage = view.findViewById(R.id.EventImage);
        CreateEventButton = view.findViewById(R.id.CreateEvent);
        CreateEventButton.setOnClickListener(this);
        EventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog,
                        onTimeSetListener,
                        hour, minute, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                String time = hour + ":" + minute;
                SimpleDateFormat f24Hours = new SimpleDateFormat(
                        "HH:mm"
                );
                try {
                    Date date = f24Hours.parse(time);
                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                            "hh:mm aa"
                    );
                    EventTime.setText(f12Hours.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        EventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog,
                        onDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d("CreateEvent", "onDateSet: MM/dd/yyyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                EventDate.setText(date);
            }
        };
    }

    /**
     * Allows for the clicked to edit on the text box
     *
     * @param v The textView clicked
     * @return nothing
     */
    @Override
    public void onClick(View v) {

        JSONObject newEventData = new JSONObject();
        final String commentsView = new String();

        try {
            newEventData.put("name", EventName.getText());
            newEventData.put("description", EventDescription.getText());
            newEventData.put("time", EventTime.getText());
            newEventData.put("date", EventDate.getText());
            newEventData.put("club", clubData.getString("clubID"));
            newEventData.put("commentsView", commentsView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Creating Data for CommentsView
        JSONArray comments = new JSONArray();
        String eventID = new String();

        try {
            newEventData.put("eventID", eventID);
            newEventData.put("comments", comments);
            newEventData.put("imageurl", EventImage.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, RestSingleton.getInstance(view.getContext()).getUrl() + "createEvent", newEventData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            EventFrag frag = new EventFrag();
                            Bundle data = new Bundle();
                            data.putString("eventID", response.getString("eventID"));
                            data.putString("clubID", clubData.getString("clubID"));
                            data.putString("commentsViewID", response.getString("commentsViewID"));
                            frag.setArguments(data);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
                        } catch (JSONException e) {
                            Log.d("Error getting eventID", String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error connecting", String.valueOf(error));
            }
        });
        RestSingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

        //hide keyboard
        if (view != null && getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
