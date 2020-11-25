package com.example.cs4532.umdalive.fragments.base;
// untested imports


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * @author Henry Trinh, Josh Tindell
 * <p>
 * 11/6/2020 Proceed with caution
 * -database functions and code needs to be looked at in this file
 * -need to make sure things are running
 */

public class CommentsFrag extends Fragment {
    //View
    View view;

    //Layout Components
    private TextView profileName;
    private TextView userComment;
    private TextView timePosted;
    private Button goToComments;
    //private ImageView profileImage;
    private FloatingActionButton commentEditFAB;


    /**
     * Create the comment page when navigating to it
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create View
        view = inflater.inflate(R.layout.comment_layout, container, false);

        //Get Layout Components
        getLayoutComponents();

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

        //Return View
        return view;
    }


    /**
     * Gets the layout components from comments_layout.xml
     *
     * @return nothing
     */
    private void getLayoutComponents() {
        profileName = (TextView) view.findViewById(R.id.CommentUsername);
        timePosted = (TextView) view.findViewById(R.id.CommentTime);
        userComment = (TextView) view.findViewById(R.id.CommentDescriptionView);
        userComment.setMovementMethod(new ScrollingMovementMethod());
        commentEditFAB = (FloatingActionButton) view.findViewById(R.id.EditCommentFAB);

        goToComments = view.findViewById(R.id.GoToComment);
        goToComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = (String) goToComments.getTag();
                CommentsViewFrag frag = new CommentsViewFrag();
                Bundle data = new Bundle();
                data.putString("commentsViewID", TAG);
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
            }
        });
    }

    private void updateUI(JSONObject res) throws JSONException {
        getActivity().findViewById(R.id.PageLoading).setVisibility(View.GONE);
        userComment.setText(res.getString("comment"));
        userComment.setTag(res.getString("_id"));
        profileName.setText(res.getString("name"));
        profileName.setTag(res.getString("_id"));
        timePosted.setText(res.getString("time"));
        goToComments.setTag(res.getJSONObject("commentsView").getString("_id"));

        profileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFrag frag = new ProfileFrag();
                Bundle data = new Bundle();
                data.putString("userID", UserSingleton.getInstance().getUserID());
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
            }
        });

        if (res.getJSONObject("commentsView").getJSONObject("eventID").getJSONObject("club").getJSONObject("members").getString("admin").equals(UserSingleton.getInstance().getUserID()) || UserSingleton.getInstance().getUserID().equals(res.getString("name"))) {
            commentEditFAB.setVisibility(View.VISIBLE);
        } else {
            commentEditFAB.setVisibility(View.GONE);
        }
    }
}


