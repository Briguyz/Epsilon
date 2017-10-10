package com.example.cs4532.umdalive.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cs4532.umdalive.Presenters.Presenter;
import com.example.cs4532.umdalive.R;

import java.util.ArrayList;

public class SearchClubsView extends AppCompatActivity {
    ListView listView;
    Presenter presenter;
    private Object keywordItem = new Object();

    public static final String CLUB_NAME = "com.example.kevin.umdalive.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this);
        setContentView(R.layout.activity_search_clubs_view);
        listView = (ListView) findViewById(R.id.list2);

        Spinner spinner = (Spinner) findViewById(R.id.keywordChooser); // Create an ArrayAdapter using the string array and a default spinner layout
        //keyword_list is the list of all the club categories. We should probably expand this at some point and add on "other" option.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.keyword_list2, android.R.layout.simple_spinner_item); // Specify the layout to use when the list of choices appears

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Apply the adapter to the spinner

        spinner.setAdapter(adapter);
        //dealing with the selected option of the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                keywordItem = parent.getItemAtPosition(pos);
                setView();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        ArrayList<String> clubNames = presenter.getSearchClubNames((String) keywordItem);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clubNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(com.example.cs4532.umdalive.Views.SearchClubsView.this, DisplayClubView.class);
                intent.putExtra(CLUB_NAME, itemValue);
                startActivity(intent);
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