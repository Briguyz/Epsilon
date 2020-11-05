package com.example.cs4532.umdalive.fragments.base;
// untested imports
import java.util.Calendar;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.create.CreateCommentsFrag;
import com.example.cs4532.umdalive.fragments.edit.EditProfileFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentsFrag extends Fragment {
    //View
    View view;

    //Layout Components
    private TextView profileName;
    private TextView userComment;
    private TextView timePosted;
    private LinearLayout commentBoxShow;

    private FloatingActionButton addCommentButton;


    //waiting for testing development
    private ImageView profileImage;
    private FloatingActionButton commentEdit;

    private Bundle commentData;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create View
        view = inflater.inflate(R.layout.comment_layout, container, false);

        //Get Layout Components
        getLayoutComponents();

        commentData = new Bundle();

        //10/30 needs more developement help for editing
        /* had to comment this out because of failure to work
        commentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileFrag frag = new EditProfileFrag();
                frag.setArguments(commentData);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
            }
        });
        */

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
         * @return nothing
         */
        private void getLayoutComponents() {
            //prototype of teh commentbox
            //currently broken
            //commentBoxShow = view.findViewById(R.id.commentsSection);

            //prototype of the addcommentsection
            addCommentButton = view.findViewById(R.id.addCommentButtonView);
            addCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String TAG = (String) addCommentButton.getTag();
                    CreateCommentsFrag frag = new CreateCommentsFrag();
                    Bundle data = new Bundle();
                    data.putString("commentsID", TAG);
                    frag.setArguments(data);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
                }
            });
        }

    private void updateUI(JSONObject res) throws JSONException {
        getActivity().findViewById(R.id.PageLoading).setVisibility(View.GONE);

        }
    }


