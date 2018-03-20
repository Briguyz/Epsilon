package com.example.cs4532.umdalive.Views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.cs4532.umdalive.Models.UserInformationModel;
import com.example.cs4532.umdalive.Presenters.Presenter;
import com.example.cs4532.umdalive.R;

import java.util.ArrayList;
/**
 * Created by Gator on 11/29/2017.
 */

public class NewUserDataView extends AppCompatActivity {
    Presenter presenter;
    Spinner major;
    Spinner gradDate;
    String graduationDate;
    String Major;
    String[] interestList;
    Object graduationItem;
    Object majorItem;
    ArrayList mSelectedItems;
    String email;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter();
        setContentView(R.layout.activity_userdata);
        email = getIntent().getStringExtra("Email");
        name = getIntent().getStringExtra("Name");
        major = (Spinner) findViewById(R.id.spinnermajor);
        gradDate = (Spinner) findViewById(R.id.spinnergrad);

        interestList = getResources().getStringArray(R.array.list_of_interests);
        ArrayAdapter<CharSequence> gradAdapter = ArrayAdapter.createFromResource(this, R.array.graduation_date, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> majorAdapter = ArrayAdapter.createFromResource(this, R.array.major_list, android.R.layout.simple_spinner_item);
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        major.setAdapter(majorAdapter);
        gradDate.setAdapter(gradAdapter);
        gradDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                graduationItem = parent.getItemAtPosition(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                majorItem = parent.getItemAtPosition(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        convertToStrings();
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("major", Major);
        edit.putString("gradDate", graduationDate);
        edit.apply();
        presenter.putUser(name,Major,email,graduationDate);
    }

    public void convertToStrings() {
        Major = major.getSelectedItem().toString();
        graduationDate = gradDate.getSelectedItem().toString();

    }

    public void Interests(View view) {
        Dialog x = onCreateDialog();
        x.show();
        //presenter.UserDataView(Major,gradDatee);
    }

    public Dialog onCreateDialog() {
        mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle("Choose your interests")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.list_of_interests, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(interestList[which]);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove((Integer) Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
    public void onFinishProfile(View view){
        Intent intent = new Intent(this, MainView.class);
        intent.putExtra("Email", presenter.getThisUser());
        intent.putExtra("Name", name);
        startActivity(intent);
    }

    public ArrayList<String> getmSelectedItems() {
        return mSelectedItems;
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

