package com.example.cs4532.umdalive.fragments.edit;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.base.AllClubsFrag;
import com.example.cs4532.umdalive.fragments.base.ClubFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Object;
/**
 * Created by Josh on 4/25/2018.
 */

/**
 * @author Josh Senst
 *
 * 4/26/2018
 *
 * Class that creates the Edit Club Page
 */
public class EditClubFrag extends Fragment implements View.OnClickListener {
    //View
    View view;

    //Layout Components
    private TextView EditingClub;
    private EditText NewClubName;
    private EditText NewClubDescription;
    private EditText NewImageURL;
    private Button SaveButton;
    private Button DeleteButton;

    private JSONObject clubData;

    /**
     * Creates the page whenever the button for editing a club is pressed
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view The view of the edit club page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Create View
        view = inflater.inflate(R.layout.edit_club_layout, container, false);

        //Get Layout Components
        getLayoutComponents();

        //Use Volley Singleton to Update Page UI
        RestSingleton restSingleton = RestSingleton.getInstance(view.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, restSingleton.getUrl() + "getClub/" + getArguments().getString("clubID"),
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
     * Allows the clicking of the editText boxes
     * @param v The text editing area that has been clicked
     * @return nothing
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //Delete Club Case
            case R.id.DeleteClub:
            RestSingleton restSingleton = RestSingleton.getInstance(view.getContext());
            StringRequest stringRequest = null;
            try {
                stringRequest = new StringRequest(Request.Method.DELETE, restSingleton.getUrl() + "deleteClub/" + clubData.getString("_id"),
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
            //Thread is put to sleep for the request
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AllClubsFrag frag = new AllClubsFrag();
            Bundle data = new Bundle();
            frag.setArguments(data);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();

            //Toast for when the action is complete
            try {
                Toast.makeText(view.getContext(), "\"" + clubData.getString("name") + "\"" + " was successfully deleted.", Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
               e.printStackTrace();
            }

            break;
        //Save Event Case
        case R.id.SaveClub:
            String clubid = null;
            try {
                clubid = clubData.getString("_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (NewClubName.getText().toString().trim().length() != 0) {
                try {
                    Log.d("Name", NewClubName.getText().toString());
                    clubData.put("name", NewClubName.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (NewClubDescription.getText().toString().trim().length() != 0) {
                try {
                    Log.d("Description", NewClubDescription.getText().toString());
                    clubData.put("description", NewClubDescription.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (NewImageURL.getText().toString().trim().length() != 0) {
                try {
                    Log.d("ProfilePic", NewImageURL.getText().toString());
                    clubData.put("profilePic", NewImageURL.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Gets the members and sends them to the server
            try {
                String jsonstring = "{\"admin\": \" \", \"regular\": []}}";
                JSONObject members = new JSONObject(jsonstring);

                //Adding admin
                String admin = clubData.getJSONObject("members").getJSONObject("admin").getString("userID");
                members.put("admin", admin);

                //Adding regulars
                JSONArray regulars = clubData.getJSONObject("members").getJSONArray("regular");
                JSONArray regular = new JSONArray();
                
                for(int i = 0; i < regulars.length(); i++){
                    regular.put(i, regulars.getJSONObject(i).getString("userID"));
                }
                members.put("regular", regular);

                clubData.put("members", members);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Gets the events and sends them to the sever
            try {
                JSONArray eventsarray = clubData.getJSONArray("events");
                JSONArray events = new JSONArray();
                for(int i = 0; i < eventsarray.length(); i++){
                    events.put(i, eventsarray.getJSONObject(i).getString("_id"));
                    }

                clubData.put("events", events);
                } catch (JSONException e) {
                    e.printStackTrace();
                    }

            restSingleton = RestSingleton.getInstance(view.getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, restSingleton.getUrl() + "editClub/", clubData,
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
            //Thread is put to sleep for the request
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            frag = new AllClubsFrag();
            data = new Bundle();
            data.putString("clubID", clubid);
            frag.setArguments(data);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();

            //hide keyboard
            if (view != null && getActivity() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            try {
                Toast.makeText(view.getContext(), "\"" + clubData.getString("name") +"\"" +
                        " was successfully edited.", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            break;}
    }


    /**
     * Gets the layout components from edit_club_layout.xml
     * @return nothing
     */
    private void getLayoutComponents() {
        EditingClub = view.findViewById(R.id.ClubEditing);
        NewClubName = view.findViewById(R.id.NewClubName);
        NewClubDescription = view.findViewById(R.id.NewClubDescription);
        NewImageURL = view.findViewById(R.id.NewImageURL);
        SaveButton = view.findViewById(R.id.SaveClub);
        SaveButton.setOnClickListener(this);
        DeleteButton = view.findViewById(R.id.DeleteClub);
        DeleteButton.setOnClickListener(this);
    }

    /**
     * Adds the layout components and sets them in editTexts
     * @param res The response from the database
     * @throws JSONException Error in JSON processing
     * @see JSONException
     */
    private void updateUI(JSONObject res) throws JSONException {
        EditingClub.setText( res.getString("name"));
        DeleteButton.setTag("DELETE");
        NewClubName.setText(res.getString("name"));
        NewClubDescription.setText(res.getString("description"));
        NewImageURL.setText(res.getString("profilePic"));
        clubData = res;
    }
}
