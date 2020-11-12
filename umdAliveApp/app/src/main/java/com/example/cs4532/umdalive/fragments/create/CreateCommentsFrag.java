package com.example.cs4532.umdalive.fragments.create;

import android.support.v4.app.Fragment;
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


/**11/6Code is buggy with reponse to server
 * -will crash the server
 * -Henry, Josh
 */
public class CreateCommentsFrag  extends Fragment implements View.OnClickListener {
    //View
    View view;

    //Layout Components
    //private EditText EventName;
    //private EditText EventDescription;
    private EditText CommentText;
    private Button CreateCommentButton;
    private JSONObject eventData;

    /**
     * Creates the page for Editing Comments when the edit events button is pressed
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view The View of the create events page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Create View
        view = inflater.inflate(R.layout.create_comment_layout, container, false);

        //Set Comment Data
        try {
            eventData = new JSONObject();
            eventData.put("eventID", getArguments().getString("eventID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Get Layout Components
        getLayoutComponents();

        //Return View
        return view;
    }

    /**
     * Allows for the clicked to edit on the text box
     * @param v The textView clicked
     * @return nothing
     */
    @Override
    public void onClick(View v) {
        JSONObject newCommentData = new JSONObject();
        try {
            newCommentData.put("comment", CommentText.getText());
            //newEventData.put("description", EventDescription.getText());
            //newEventData.put("time",EventTime.getText());
            //newEventData.put("date", EventDate.getText());
            newCommentData.put("event",eventData.getString("eventID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, RestSingleton.getInstance(view.getContext()).getUrl() + "createcomment", newCommentData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            CommentsFrag frag = new CommentsFrag();
                            Bundle data = new Bundle();
                            data.putString("commentID", response.getString("commentID"));
                            data.putString("eventID", eventData.getString("eventID"));
                            frag.setArguments(data);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
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


    /**
     * Gets the layout components from edit_comments_layout.xml
     * @return nothing
     */
    private void getLayoutComponents() {
        CommentText = view.findViewById(R.id.addCommentTextBox);
        //EventDescription = view.findViewById(R.id.EventDescription);
        //EventTime = view.findViewById(R.id.EventTime);
        //EventDate = view.findViewById(R.id.EventDate);
        CreateCommentButton = view.findViewById(R.id.saveCommentButton);
        CreateCommentButton.setOnClickListener(this);
    }

}