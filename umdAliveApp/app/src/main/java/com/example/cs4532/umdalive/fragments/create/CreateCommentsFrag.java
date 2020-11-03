package com.example.cs4532.umdalive.fragments.create;

import android.app.Fragment;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.base.CommentsFrag;
import com.example.cs4532.umdalive.fragments.base.EventFrag;

import org.json.JSONException;
import org.json.JSONObject;



public class CreateCommentsFrag extends Fragment implements View.OnClickListener {
    //View
    View view;
    //Layout Components
    private EditText commentItself;
    private Button CreateCommentButton;
    private JSONObject commentData;

    /**
     * Creates the create profile page view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view The view of the Create profile page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Create View
        view = inflater.inflate(R.layout.create_profile_layout, container, false);

        getActivity().findViewById(R.id.PageLoading).setVisibility(View.GONE);

        try {
            commentData = new JSONObject();
            commentData.put("commentID", getArguments().getString("commentID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getLayoutComponents();

        return view;
    }

    /**
     * Allows for the clicked to edit on the text box
     * @param v The textView clicked
     * @return nothing
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        JSONObject newCommentData = new JSONObject();
        try {
            newCommentData.put("commentString", commentItself.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, RestSingleton.getInstance(view.getContext()).getUrl() + "createComment", newCommentData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            CommentsFrag frag = new CommentsFrag();
                            Bundle data = new Bundle();
                            data.putString("eventID", response.getString("eventID"));
                            data.putString("commentID", commentData.getString("commentID"));
                            frag.setArguments(data);
                            getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
                        } catch (JSONException e){
                            Log.d("Error getting commentID", String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error connecting", String.valueOf(error));
            }
        });

        RestSingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getLayoutComponents() {
        CreateCommentButton = view.findViewById(R.id.saveCommentButton);
        CreateCommentButton.setOnClickListener(this);
    }

}
