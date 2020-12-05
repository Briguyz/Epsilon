package com.example.cs4532.umdalive.fragments.create;

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
import android.widget.EditText;
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
import com.example.cs4532.umdalive.fragments.base.ProfileFrag;
import com.example.cs4532.umdalive.fragments.create.CreateEventFrag;
import com.example.cs4532.umdalive.fragments.edit.EditClubFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.*;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class CreateReportFrag extends Fragment {
    //View
    View view;

    //Layout Components
    private EditText reportMSG;
    private EditText description;
    private Button sendReport;


    //creates the report page when clicking on it
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Create View
        view = inflater.inflate(R.layout.report_layout, container, false);

        getActivity().findViewById(R.id.PageLoading).setVisibility(View.GONE);

        //Get Layout Components
        getLayoutComponents();

        return view;
    }

    //gets layout from report_layout.xml
    private void getLayoutComponents() {
        reportMSG = view.findViewById(R.id.reasonReporting);
        description = view.findViewById(R.id.description);
        sendReport = view.findViewById(R.id.sendReport);
        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject newReportData = new JSONObject();

                try {
                    newReportData.put("message", reportMSG.getText());
                    newReportData.put("description", description.getText());
                    newReportData.put("userID", UserSingleton.getInstance().getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, RestSingleton.getInstance(view.getContext()).getUrl() + "createReport", newReportData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    ProfileFrag frag = new ProfileFrag();
                                    Bundle data = new Bundle();
                                    data.putString("reportID", response.getString("reportID"));
                                    data.putString("userID", UserSingleton.getInstance().getUserID());
                                    frag.setArguments(data);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
                                } catch (JSONException e) {
                                    Log.d("Error getting reportID", String.valueOf(e));
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error connecting", String.valueOf(error));
                    }
                });
                RestSingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
                Toast.makeText(view.getContext(), "Report was successfully sent", Toast.LENGTH_LONG).show();
            }
        });
    }
}
