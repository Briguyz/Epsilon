package com.example.cs4532.umdalive.fragments.base;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.create.CreateEventFrag;
import com.example.cs4532.umdalive.fragments.edit.EditClubFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.*;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Requires argument with key of clubID to be passed into it before it is added to the frame layout
 */

/**
 * @author Josh Senst
 *
 * 4/26/2018
 *
 * Class that creates the club page
 */
public class ClubFrag extends Fragment{

    //View
    View view;

    //Layout Components
    private ImageView clubImage;
    private TextView clubName;

    private Button joinLeave;
    private TextView clubDescription;

    private LinearLayout members;
    private LinearLayout eventsList;

    private FloatingActionButton editClub;
    private FloatingActionButton addEvent;

    private JSONObject joinLeaveObj;

    // Function to validate image string
    public boolean isValid(String str) {
            String regex = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)";

            Pattern p = Pattern.compile(regex);

            if (str == null) { return false; }

            Matcher m = p.matcher(str);

            return m.matches();
        }

    /**
     * Creates the page whenever the club page is clicked on
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view The view of the club page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         //Create View
        view = inflater.inflate(R.layout.club_layout, container, false);
        view.setVisibility(View.GONE);

        joinLeaveObj = new JSONObject();

        try {
            joinLeaveObj.put("clubID", getArguments().getString("clubID"));
            joinLeaveObj.put("userID", UserSingleton.getInstance().getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Get Layout Components
        getLayoutComponents();

        //On Click
        editClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditClubFrag frag = new EditClubFrag();
                Bundle data = new Bundle();
                data.putString("clubID", clubName.getTag().toString());
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEventFrag frag = new CreateEventFrag();
                Bundle data = new Bundle();
                data.putString("clubID", clubName.getTag().toString());
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
            }
        });

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
     * Gets the layout components from club_layout.xml
     * @return nothing
     */
    private void getLayoutComponents() {
        clubImage = view.findViewById(R.id.clubImage);
        clubName = view.findViewById(R.id.ClubNameView);
        clubDescription = view.findViewById(R.id.DescriptionView);
        joinLeave= view.findViewById(R.id.ClubJoinLeave);
        members = view.findViewById(R.id.memberList);
        eventsList = view.findViewById(R.id.eventsList);
        editClub = view.findViewById(R.id.EditClub);
        addEvent = view.findViewById(R.id.AddEvent);
    }

    /**
     * Adds the club information to the page depending on which club was clicked. Inside there are several onClicks relevant to different items in lists being
     * clicked within the club layout.
     * @param res The response from the database
     * @throws JSONException Error in JSON processing
     * @see JSONException
     */
    private void updateUI(JSONObject res) throws JSONException{
        view.setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.PageLoading).setVisibility(View.GONE);

        //Sets the club image
        if (res.getString("profilePic") != null && isValid(res.getString("profilePic"))) {
            Log.d("imageurl", res.getString("profilePic"));
            Glide.with(this)
                    .load(res.getString("profilePic"))
                    .error(R.drawable.ic_menu_all_clubs)
                    .transform(new RoundedCornersTransformation(30, 10))
                    .into(clubImage);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_menu_all_clubs)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    //.load("https://images.homedepot-static.com/productImages/42613c1a-7427-4557-ada8-ba2a17cca381/svn/gorilla-carts-yard-carts-gormp-12-64_1000.jpg")
                    .into(clubImage);
        }

        clubName.setText(res.getString("name"));
        clubName.setTag(res.getString("_id"));

        clubDescription.setText(res.getString("description"));

        JSONObject memberJson = res.getJSONObject("members");

        JSONArray regulars = memberJson.getJSONArray("regular");
        final JSONObject admins = memberJson.getJSONObject("admin");

        JSONArray events = res.getJSONArray("events");

        String userID = UserSingleton.getInstance().getUserID();


        joinLeave.setText("Join Club");
        joinLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinClub();
            }
        });

            for (int i = 0; i < events.length(); i++) {
                final JSONObject event = events.getJSONObject(i);

                String name = event.getString("name");
                String day = event.getString("date");

                TextView eventText = new TextView(view.getContext());

                eventText.setText(name + " : " +day);
                eventText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                eventText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventFrag frag = new EventFrag();
                        Bundle data = new Bundle();
                        try {
                            data.putString("eventID", event.get("_id").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        frag.setArguments(data);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
                    }       });

                eventsList.addView(eventText);
            }



        if(admins.getString("userID").equals(userID)){
            editClub.setVisibility(View.VISIBLE);
            addEvent.setVisibility(view.VISIBLE);
            joinLeave.setVisibility(View.GONE);
        }

        TextView adminText = new TextView(view.getContext());
        adminText.setText(admins.get("name").toString() + " : ADMIN");
        adminText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        adminText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFrag frag = new ProfileFrag();
                Bundle data = new Bundle();
                try {
                    data.putString("userID", admins.get("userID").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                frag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
            }
        });
        members.addView(adminText);

        for (int i=0;i<regulars.length();i++){
            final JSONObject member = regulars.getJSONObject(i);
            TextView memberText = new TextView(view.getContext());
            memberText.setText(member.get("name").toString());
            memberText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            System.out.println(member.get("userID").toString() +" : "+ userID);
            if(member.get("userID").toString().equals(userID)){
                joinLeave.setText("Leave Club");
                joinLeave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        leaveClub();
                    }
                });
            }

            memberText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFrag frag = new ProfileFrag();
                    Bundle data = new Bundle();
                    try {
                        data.putString("userID", member.get("userID").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    frag.setArguments(data);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
                }
            });
            members.addView(memberText);
        }
    }

    private void joinClub() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, RestSingleton.getInstance(view.getContext()).getUrl() + "joinClub", joinLeaveObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        joinLeave.setText("Leave Club");
                        joinLeave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                leaveClub();
                            }
                        });

                        TextView userText = new TextView(view.getContext());
                        userText.setText(UserSingleton.getInstance().getName());
                        userText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                        members.addView(userText);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error connecting", String.valueOf(error));
            }
        });

        RestSingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        //Toast for when the action is complete
        Toast.makeText(view.getContext(), "You successfully joined " + "\"" + clubName.getText().toString() + "\"."
                , Toast.LENGTH_LONG).show();
    }

    private void leaveClub() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, RestSingleton.getInstance(view.getContext()).getUrl() + "leaveClub", joinLeaveObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProfileFrag frag = new ProfileFrag();
                        Bundle data = new Bundle();
                        data.putString("userID", UserSingleton.getInstance().getUserID());
                        frag.setArguments(data);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error connecting", String.valueOf(error));
            }
        });

        RestSingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        //Toast for when the action is complete
        Toast.makeText(view.getContext(), "You successfully left " + "\"" + clubName.getText().toString() + "\"."
                , Toast.LENGTH_LONG).show();
    }
}