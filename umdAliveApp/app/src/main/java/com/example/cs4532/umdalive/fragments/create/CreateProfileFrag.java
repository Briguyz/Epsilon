package com.example.cs4532.umdalive.fragments.create;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.base.ProfileFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ross Schultz
 *
 * 4/26/2018
 *
 * Class that creates the page to make a profile
 */
public class CreateProfileFrag extends Fragment {

    //View
    View view;

    private TextView name;
    private EditText major;
    private EditText about;
    private Button save;


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

        getLayoutComponents();

        name.setText(UserSingleton.getInstance().getName());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (major.getText().length() > 0 && about.getText().length() > 0) {
                    try {
                        createUser();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }

    /**
     * Gets the layout components from create_profile_layout.xml
     * @return nothing
     */
    //Sets the Text views of the profile layout
    private void getLayoutComponents() {
        name = view.findViewById(R.id.createProfileName);
        major = view.findViewById(R.id.createProfileMajor);
        about = view.findViewById(R.id.createProfileAbout);
        save = view.findViewById(R.id.createProfileSave);
    }

    /**
     * Upon sign-in this will create the user, and put their information into one of the application's profiles.
     * @throws JSONException Error in JSON Processing
     * @see JSONException
     * @return nothing
     */
    private void createUser() throws JSONException {
        JSONObject newUserData = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        newUserData.put("name", UserSingleton.getInstance().getName());
        newUserData.put("email", UserSingleton.getInstance().getEmail());
        newUserData.put("major", major.getText());
        newUserData.put("userID", UserSingleton.getInstance().getUserID());
        newUserData.put("description", about.getText());
        newUserData.put("profilePic", UserSingleton.getInstance().getProfileUrl());
        newUserData.put("clubs", jsonArray);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, RestSingleton.getInstance(view.getContext()).getUrl() + "createUser", newUserData,
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
        //hide keyboard
        if (view != null && getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
