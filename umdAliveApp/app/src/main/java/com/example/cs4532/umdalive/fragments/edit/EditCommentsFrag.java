package com.example.cs4532.umdalive.fragments.edit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.fragments.base.CommentsViewFrag;

import org.json.JSONException;
import org.json.JSONObject;

public class EditCommentsFrag extends Fragment implements View.OnClickListener {

    //View
    View view;

    //Layout Components
    private EditText editComments;
    private Button saveComment;
    private Button deleteComment;
    private Button goToComments;
    private JSONObject commentData;

    //taken from EditClubFrag and edited for comments
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Create View
        view = inflater.inflate(R.layout.edit_comment_layout, container, false);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Delete Comment Case
            case R.id.deleteComment:
                RestSingleton restSingleton = RestSingleton.getInstance(view.getContext());
                StringRequest stringRequest = null;
                try {
                    stringRequest = new StringRequest(Request.Method.DELETE, restSingleton.getUrl() + "deleteComment/" + commentData.getString("_id"),
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                restSingleton.addToRequestQueue(stringRequest);
                //Thread is put to sleep to allow request to be fulfilled so it doesn't reference null
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Creates the commentsView to go back into
                CommentsViewFrag frag = new CommentsViewFrag();
                Bundle data = new Bundle();
                try {
                    data.putString("commentsViewID", commentData.getJSONObject("commentsView").getString("_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
                Toast.makeText(view.getContext(), "Comment was successfully deleted.", Toast.LENGTH_LONG).show();
                
                //hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                break;

            //Save Comment Case
            case R.id.saveComment:
                String commentsviewid = null;
                try {
                    commentsviewid = commentData.getJSONObject("commentsView").getString("_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (editComments.getText().toString().trim().length() != 0) {
                    try {
                        Log.d("Comment", editComments.getText().toString());
                        commentData.put("comment", editComments.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    commentData.put("commentsView", commentsviewid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                restSingleton = RestSingleton.getInstance(view.getContext());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, restSingleton.getUrl() + "editComment/", commentData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error connecting", String.valueOf(error));
                    }
                });
                restSingleton.addToRequestQueue(jsonObjectRequest);

                //Thread is put to sleep to allow request to be fulfilled so it doesn't reference null
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Creates the commentsView to go back to
                frag = new CommentsViewFrag();
                data = new Bundle();
                data.putString("commentsViewID", commentsviewid);
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();

                //Toast for when action is complete
                Toast.makeText(view.getContext(), "Comment was successfully edited.", Toast.LENGTH_LONG).show();
                break;

            //Go back to CommentsView Case
            case R.id.backToComments:
                //Creates the commentsView to go back into
                frag = new CommentsViewFrag();
                data = new Bundle();
                try {
                    data.putString("commentsViewID", commentData.getJSONObject("commentsView").getString("_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
                break;
        }

    }

    private void getLayoutComponents() {
        editComments = view.findViewById(R.id.editCommentBox);
        deleteComment = view.findViewById(R.id.deleteComment);
        saveComment = view.findViewById(R.id.saveComment);
        goToComments = view.findViewById(R.id.backToComments);
        deleteComment.setOnClickListener(this);
        saveComment.setOnClickListener(this);
        goToComments.setOnClickListener(this);
    }

    private void updateUI(JSONObject res) throws JSONException {
        editComments.setText(res.getString("comment"));
        deleteComment.setTag("DELETE");
        commentData = res;
    }
}