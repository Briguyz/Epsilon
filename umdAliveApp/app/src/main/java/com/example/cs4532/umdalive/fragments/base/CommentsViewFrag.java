package com.example.cs4532.umdalive.fragments.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.create.CreateCommentsFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private TextView commentViewName;
    private RecyclerView commentBoxShow;
    private FloatingActionButton addCommentButton;
    private Button goToEventButton;
    private ProgressBar commentProgressCosmetic;
    private ArrayList<CommentFragMaker> commentArray;

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

        //Use Volley Singleton to Update Page UI
        RestSingleton restSingleton = RestSingleton.getInstance(view.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, restSingleton.getUrl() + "getCommentsView/" + getArguments().getString("commentsViewID"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            updateUI(new JSONObject(response));
                            CommentFragAdapter adapter = new CommentFragAdapter(view.getContext(),commentArray);
                            RecyclerView.LayoutManager CommentLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false );
                            commentBoxShow.setLayoutManager(CommentLayoutManager);
                            commentBoxShow.setAdapter(adapter);
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
        commentViewName = (TextView) view.findViewById(R.id.commentHeader);
        commentBoxShow =  view.findViewById(R.id.comment_ViewSection);
        addCommentButton = view.findViewById(R.id.addCommentButtonView);
        commentProgressCosmetic = view.findViewById(R.id.commentProgressBar);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = (String) commentViewName.getTag();
                CreateCommentsFrag frag = new CreateCommentsFrag();
                Bundle data = new Bundle();
                data.putString("commentsViewID", TAG);
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
        view.setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.PageLoading).setVisibility(View.GONE);
        goToEventButton.setTag(res.getJSONObject("eventID").getString("_id"));
        commentViewName.setTag(res.getString("_id"));

        commentArray = new ArrayList<>();
        JSONArray comments = res.getJSONArray("comments");
        Log.d("comment.length", comments.length() + "");

        for(int i = 0; i < comments.length(); i++){
            Log.d("give me the comments", comments.get(i).toString());
            JSONObject indv = comments.getJSONObject(i);
            
            //final JSONObject indv = comments.getJSONObject(i);
            //Log.d("tag", comments.getJSONObject(i).toString());
            comments.get(i);
            String name = comments.getJSONObject(i).getString("name");
            String id = comments.getJSONObject(i).getString("_id").toString();
            String userComment = comments.getJSONObject(i).getString("comment").toString();
            String userTime = comments.getJSONObject(i).getString("time").toString();
            CommentFragMaker indiviualComment = new CommentFragMaker("empty", name, userComment, userTime);
            commentArray.add(indiviualComment);
            CommentFragAdapter adapter = new CommentFragAdapter(view.getContext(),commentArray);
            commentBoxShow.setAdapter(adapter);

        }



        CommentFragAdapter adapter = new CommentFragAdapter(view.getContext(),commentArray);
        RecyclerView.LayoutManager CommentLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false );
        commentBoxShow.setLayoutManager(CommentLayoutManager);
        commentBoxShow.setAdapter(adapter);


        //This statement is for the progress bar after grabbing data
        if(commentArray.size() == 0) {
            commentProgressCosmetic.setVisibility(View.VISIBLE);
        } else {
            commentProgressCosmetic.setVisibility(View.GONE);
        }
    }

    /**
     *
     * @param res
     * @throws JSONException
     */
    public void addTestData(JSONObject res) throws JSONException {
        JSONArray comments = res.getJSONArray("comments");
        for (int i = 0; i < comments.length(); i++) {
            final JSONObject comment = comments.getJSONObject(i);

            String name = comment.getString("name");
            String time = comment.getString("time");
            String userComment = comment.getString("comment");

            commentArray.add(new CommentFragMaker("henry", name, userComment, time));
            Log.d("made it", "Made it");
        }

        commentBoxShow.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false ));
        CommentFragAdapter adapter = new CommentFragAdapter(view.getContext(),commentArray);
        commentBoxShow.setAdapter(adapter);
    }

    public void addTest2Data() {
        commentArray.add(new CommentFragMaker("henry", "i69", "stupid", "ye"));
    }

    public void tester() {
        //Use Volley Singleton to Update Page UI
        RestSingleton restSingleton = RestSingleton.getInstance(view.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, restSingleton.getUrl() + "getComment/" + getArguments().getString("commentID"),
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
    }
}


