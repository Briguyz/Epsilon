package com.example.cs4532.umdalive.fragments.base;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.fragments.create.CreateCommentsFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Henry Trinch, Josh Tindell, Jacob Willmsen, Brian Zhagnay
 *
 * 11/16/2020 Proceed with caution
 * Version 1.0
 *
 * This class is a view that shows all the comments for that event
 *
*/

public class CommentsViewFrag extends Fragment {
    //View
    View view;

    //Layout Components
    private ListView commentBoxShow;
    private FloatingActionButton addCommentButton;
    private Button goToEventButton;
    private Bundle commentData;

    /**
     * Create the comment view page when navigating to it
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create View
        view = inflater.inflate(R.layout.commentview_layout, container, false);

        //Get Layout Components
        getLayoutComponents();

        commentData = new Bundle();

        //Use Volley Singleton to Update Page UI

        RestSingleton restSingleton = RestSingleton.getInstance(view.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, restSingleton.getUrl() + "getCommentsView/" + getArguments().getString("commentsViewID"),
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
     * Gets the layout components from commentview_layout.xml
     *
     * @return nothing
     */
    private void getLayoutComponents() {
        commentBoxShow = (ListView) view.findViewById(R.id.commentsSection);

        addCommentButton = view.findViewById(R.id.addCommentButtonView);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = (String) addCommentButton.getTag();
                CreateCommentsFrag frag = new CreateCommentsFrag();
                Bundle data = new Bundle();
                data.putString("commentID", TAG);
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
            }
        });

        // This is the button that goes from CommentView back to Events
        goToEventButton = view.findViewById(R.id.fromCommentsToEvents);
        goToEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = (String) goToEventButton.getTag();
                EventFrag frag = new EventFrag();
                Bundle data = new Bundle();
                data.putString("eventID", TAG);
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
            }
        });
    }

    /**
     * Adds comments stored in the database
     * @param res The response from the database
     * @return nothing
     * @throws JSONException Error in JSON processing
     * @see JSONException
     */
    private void updateUI(JSONObject res) throws JSONException {
        getActivity().findViewById(R.id.PageLoading).setVisibility(View.GONE);
        //JSONArray allComments = res.getJSONArray("comments");
        /* Code to Make Comments appear in here
        * for (int i=0; i<allComments.length(); i++) {
        *
        *
        *
        * }
        * */
    }
}


