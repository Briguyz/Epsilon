package com.example.kevin.umdalive;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * Created by Luke on 12/6/2016.
 */

public class DisplayClub extends AppCompatActivity {

    private static String clubName;
    private static String description;
    private static ArrayList<String> posts;
    private static String administrator;
    private static String keywords;

    public static void setDescription(String tempDescription){
        description = tempDescription;
    }

    public static void setPosts(ArrayList<String> tempPosts){
        posts = tempPosts;
    }

    public static void setAdministrator(String tempAdministrator){
        administrator = tempAdministrator;
    }

    public static void setKeywords(String tempKeywords){
        keywords = tempKeywords;
    }


    public static void setClubName(String club){
        clubName = club;
    }

    public static String getClubName(){
        return clubName;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_club);
        EditText clubnameEditText = (EditText) findViewById(R.id.display_club_name);
        EditText discriptionEditText = (EditText) findViewById(R.id.display_club_description);
        EditText keywordEditText = (EditText) findViewById(R.id.display_clubs_keyword);
        EditText administratorEditText = (EditText) findViewById(R.id.display_clubs_administator);

        clubnameEditText.setText(clubName);
        discriptionEditText.setText(description);
        keywordEditText.setText(keywords);
        administratorEditText.setText(administrator);
    }


}
