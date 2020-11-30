package com.example.cs4532.umdalive.fragments.base;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.edit.EditEventFrag;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author Josh Senst
 * <p>
 * 4/26/2018
 * <p>
 * This class holds the page for the events
 * <p>
 * 10/29/2020
 * <p>
 * view
 */
public class EventFrag extends Fragment {

    //View
    View view;

    //Layout Components
    private TextView eventName;
    private TextView eventDescription;
    private TextView eventDate;
    private TextView eventTime;
    private ImageView eventImage;

    private Button goTo;
    private FloatingActionButton editEventFAB;

    //10/29/2020 Henry Trinh, Brian, Josh
    private FloatingActionButton createCommentButton;

    // Function to validate image string
    public boolean isValid(String str) {
        String regex = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)";

        Pattern p = Pattern.compile(regex);

        if (str == null) { return false; }

        Matcher m = p.matcher(str);

        return m.matches();
    }


    /**
     * Creates the event page when navigating to it
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view The view of the event page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create View
        view = inflater.inflate(R.layout.event_layout, container, false);

        //Get Layout Components
        getLayoutComponents();

        //Use Volley Singleton to Update Page UI
        RestSingleton restSingleton = RestSingleton.getInstance(view.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, restSingleton.getUrl() + "getEvent/" + getArguments().getString("eventID"),
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

        //Return View
        return view;

    }

    /**
     * Gets the layout components from event_layout.xml
     *
     * @return nothing
     */
    private void getLayoutComponents() {
        eventName = view.findViewById(R.id.EventNameView);
        eventDate = view.findViewById(R.id.EventDateView);
        eventDescription = view.findViewById(R.id.EventDescriptionView);
        eventTime = view.findViewById(R.id.EventTimeView);
        eventImage = view.findViewById(R.id.eventurl);
        goTo = view.findViewById(R.id.GoToClub);

        goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = (String) goTo.getTag();
                ClubFrag frag = new ClubFrag();
                Bundle data = new Bundle();
                data.putString("clubID", TAG);
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
            }
        });

        createCommentButton = view.findViewById(R.id.GoToComment);


        editEventFAB =  view.findViewById(R.id.EditEventFAB);
        editEventFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = (String) eventName.getTag();
                EditEventFrag frag = new EditEventFrag();
                Bundle data = new Bundle();
                data.putString("eventID", TAG);
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
            }
        });

    }

    /**
     * Updates the UI of the Event page depending on which event had been clicked
     *
     * @param res The response from the database
     * @return nothing
     * @throws JSONException Error in JSON processing
     * @see JSONException
     */
    private void updateUI(JSONObject res) throws JSONException {
        getActivity().findViewById(R.id.PageLoading).setVisibility(View.GONE);

        //Sets the Event image
        if (res.getString("imageurl") != null && isValid(res.getString("imageurl"))) {
            Log.d("imageurl", res.getString("imageurl"));
            Glide.with(this)
                    .load(res.getString("imageurl"))
                    .error(R.drawable.ic_menu_events)
                    .transform(new RoundedCornersTransformation(30, 10))
                    .into(eventImage);
            Log.d("if", "went through the if");
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_menu_events)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    //.load("https://images.homedepot-static.com/productImages/42613c1a-7427-4557-ada8-ba2a17cca381/svn/gorilla-carts-yard-carts-gormp-12-64_1000.jpg")
                    .into(eventImage);
            Log.d("else", "went through the else");
        }
        eventName.setText(res.getString("name"));
        eventName.setTag(res.getString("_id"));
        eventDate.setText(res.getString("date"));
        eventDescription.setText(res.getString("description"));
        eventTime.setText(res.getString("time"));
        goTo.setTag(res.getJSONObject("club").getString("_id"));

        if (res.getJSONObject("club").getJSONObject("members").getString("admin").equals(UserSingleton.getInstance().getUserID())) {
            editEventFAB.setVisibility(View.VISIBLE);
        } else {
            editEventFAB.setVisibility(View.GONE);
        }

        final String commentsViewID = res.getString("commentsView");

        /**
         * Functionality of the button for goToCommentsView
         * 10/30/2020- Henry Trinh, Jacob Willmsen
         * The button switches to the commentsView associated with events
         */
       createCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentsViewFrag frag = new CommentsViewFrag();
                Bundle data = new Bundle();
                data.putString("commentsViewID", commentsViewID);
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
            }
        });
    }
}
