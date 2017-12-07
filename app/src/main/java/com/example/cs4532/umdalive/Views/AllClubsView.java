package com.example.cs4532.umdalive.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cs4532.umdalive.Presenters.Presenter;
import com.example.cs4532.umdalive.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Andrew Miller 3/21/2017
 * <p>
 * This is a view that displays all club names. When a user selects a club, DisplayClubView is launched with the correct club info.
 */
public class AllClubsView extends Activity {

    ListView listView;
    Presenter presenter;
    private String description;
    private String clubOwner;
    private String keywords;
    private String ownerEmail;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this);
        setContentView(R.layout.all_clubs);
        listView = (ListView) findViewById(R.id.list2);
        setView();
    }

    /**
     * Sets up the view.
     * <p>
     * For clarification, an ArrayAdapter is used to take the contents of clubNames and display
     * it on a default layout provided by Android(simple_list_item_1).
     * <p>
     * The listener is used to check if a club has been clicked. Once clicked, we send a get request to the server
     * and the club info is received in the response. The response is used to set the info for DisplayClubView before it is launched.
     * I don't like the way the previous group implemented this part, mainly because it doesn't work lol.
     * We will have to fix it(setDisplayClubInfo(String) in the presenter).
     */
    private void setView() {
        ArrayList<String> clubNames = presenter.getClubNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clubNames);
        //Try making a new xml for a better looking all clubs view
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);
                //have to find how to get these

                try {
                    String jsonResponse = presenter.restGet("getClub", itemValue);
                    Log.d("DisplayClub response: ", jsonResponse);
                    JSONObject clubObject = new JSONObject(jsonResponse);
                    description = clubObject.get("description").toString();
                    keywords = clubObject.get("keywords").toString();
                    ownerEmail = clubObject.get("ownerEmail").toString();
                    clubOwner = clubObject.get("clubOwner").toString();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(getApplicationContext(), "Position :" + position + "  List Item : " + itemValue, Toast.LENGTH_LONG).show();
                //if club owner, set intent to DisplayClubOwnerView.class
                if(!presenter.checkIfClubOwner(itemValue)){
                    Intent intent = new Intent (AllClubsView.this, DisplayClubOwnerView.class);
                    intent.putExtra("NAME_OF_CLUB", itemValue);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(AllClubsView.this, DisplayClubView.class);
                    intent.putExtra("NAME_OF_CLUB", itemValue);
                    startActivity(intent);
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() { //brings activity back to main screen.
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}