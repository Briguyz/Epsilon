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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.base.CommentsFrag;

import org.json.JSONException;
import org.json.JSONObject;


/**11/6Code is buggy with reponse to server
 * -will crash the server
 *11/10
 * -need more time with testing
 * -Henry, Josh
 */
public class CreateCommentsFrag  extends Fragment implements View.OnClickListener {
    //View
    View view;

    //Layout Components
    private EditText CommentText;
    private Button CreateCommentButton;
    private Button goToComments;
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
            newCommentData.put("name", UserSingleton.getInstance().getName());
            newCommentData.put("time", getCurrentTime());
            newCommentData.put("userID",UserSingleton.getInstance().getUserID());
            newCommentData.put("eventID", eventData.getString("eventID"));
            //newCommentData.put("club",clubData.getString("clubID"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Taken from CreateEventFrag since they are very similar in terms of what they do
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, RestSingleton.getInstance(view.getContext()).getUrl() + "createComment", newCommentData,
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
        CreateCommentButton = view.findViewById(R.id.saveCommentButton);
        CreateCommentButton.setOnClickListener(this);
        goToComments = view.findViewById(R.id.fromCreateCommentToComments);
        goToComments.setOnClickListener(this);
        /*
        goToComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = (String) goToComments.getTag();
                CommentsFrag frag = new CommentsFrag();
                Bundle data = new Bundle();
                data.putString("eventID", TAG);
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
            }
        });
        */
    }
    /**
     * Gets the current time for the comments posted so people can know how long ago the comments are
     * @return String strDate
     */
    private String getCurrentTime() {
        //Time parameter with calender, date import
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(currentTime);

        return strDate;
    }

}