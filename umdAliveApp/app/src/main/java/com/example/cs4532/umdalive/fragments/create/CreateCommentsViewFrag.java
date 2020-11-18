package com.example.cs4532.umdalive.fragments.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.fragments.base.CommentsViewFrag;
import com.example.cs4532.umdalive.fragments.base.EventFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jacob Willmsen
 * <p>
 * 11/16/2020
 * <p>
 * Class that creates the commentsView for comment to be displayed on
 */
public class CreateCommentsViewFrag extends Fragment {
    //View
    View view;

    //Layout Components
    private ListView commentBoxShow;
    private FloatingActionButton addCommentButton;
    private Button goToEventButton;
    private JSONObject eventData;
    private String commentsViewID;

    /**
     * Creates the commentsViewFrag where comments are displayed
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return The view of the created commentsView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Create View
        view = inflater.inflate(R.layout.commentview_layout, container, false);

        //Set CommentsView Data
        try {
            eventData = new JSONObject();
            eventData.put("eventID", getArguments().getString("eventID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Get Layout Components
        getLayoutComponents();

        //Create the commentsView in Database
        JSONArray comments = new JSONArray();
        JSONObject newCommentsViewData = new JSONObject();

        try {
            newCommentsViewData.put("eventID", eventData.getString("eventID"));
            newCommentsViewData.put("comments", comments);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, RestSingleton.getInstance(view.getContext()).getUrl() + "createCommentsView", newCommentsViewData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            commentsViewID = response.getString("commentsViewID");
                            CommentsViewFrag frag = new CommentsViewFrag();
                            Bundle data = new Bundle();
                            data.putString("commentsViewID", response.getString("commentsViewID"));
                            data.putString("eventID", eventData.getString("eventID"));
                            frag.setArguments(data);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
                        } catch (JSONException e) {
                            Log.d("Error", String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error connecting", String.valueOf(error));
            }
        });

        RestSingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

        //Return View
        return view;
    }

    private void getLayoutComponents() {
        commentBoxShow = (ListView) view.findViewById(R.id.commentsSection);
        addCommentButton = (FloatingActionButton) view.findViewById(R.id.addCommentButtonView);
        goToEventButton = (Button) view.findViewById(R.id.fromCommentsToEvents);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TAG = (String) addCommentButton.getTag();
                CreateCommentsFrag frag = new CreateCommentsFrag();
                Bundle data = new Bundle();
                data.putString("commentID", TAG);
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
            }
        });
        goToEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TAG = (String) goToEventButton.getTag();
                EventFrag frag = new EventFrag();
                Bundle data = new Bundle();
                data.putString("eventID", TAG);
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
            }
        });
    }
    public String getCommentsViewID() {
        return commentsViewID;
    }
}
