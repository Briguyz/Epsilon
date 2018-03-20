package com.example.cs4532.umdalive.Models;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Questioning this files usage
 * <p>
 * Created by Ryan on 3/17/2017.
 * <p>
 * Class to define UserInformation on sign up
 */
@SuppressWarnings("unused")
public class UserInformationModel extends AppCompatActivity {

    static private String name;
    static private String email;
    static private String major;
    static private String gradDate;
    static private String userType;
    private ArrayList<String> localClubNames;
    private ArrayList<ClubInformationModel> localClubsSubscribed;
    private ArrayList<String> localPostsSubscribed;

    public UserInformationModel() {
        name = "";
        //userType is set to guest type
        localClubsSubscribed = new ArrayList<ClubInformationModel>();
        localClubNames = new ArrayList<String>();
        localPostsSubscribed = new ArrayList<String>();
    }

    public UserInformationModel(String name, String major, String email, String gradDate, String userType, ArrayList<ClubInformationModel> tempClubsSubscribed) {
        this.name = name;
        this.email = email;
        this.major = major;
        this.gradDate = gradDate;
        this.userType = userType;
        localPostsSubscribed = new ArrayList<String>();
        localClubsSubscribed = tempClubsSubscribed;
    }

    public UserInformationModel(String name, String major, String email, String gradDate,String userType) {
        this.name = name;
        this.email = email;
        this.major = major;
        this.gradDate = gradDate;
        this.userType = userType;
    }

    /*
       Takes in a new arraylist of most recent posts then
       Removes all current posts and then replaces objects in arraylist temp
       and addes them to local array list
   */
    public void setLocalPosts(ArrayList<String> tempPostsSubscribed) {
        while (localPostsSubscribed.size() != 0) {
            localPostsSubscribed.remove(0);
        }
        for (String x : tempPostsSubscribed) {
            localPostsSubscribed.add(x);
        }
    }

    public void setLocalClubNames(ArrayList<String> names) {
        localClubNames = names;
    }

    public ArrayList<String> getLocalClubNames() {
        return localClubNames;
    }

    /**
     * getter for name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * setter for name
     *
     * @param name name be be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for email
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter for email
     *
     * @param email to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter for Major
     *
     * @return major
     */
    public String getMajor() {
        return major;
    }

    /**
     * setter for major
     *
     * @param major to be set
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * getter for gradDate
     *
     * @return gradDate
     */
    public String getGradDate() {
        return gradDate;
    }

    /**
     * setter for gradDate
     *
     * @param gradDate to be set
     */
    public void setGradDate(String gradDate) {
        this.gradDate = gradDate;
    }

    /**
     *
     * getter for userType
     * @return userType
     */
    public String getUserType(){return userType;}

    /**
     * setter for userType
     * @param userType to be set
     */
    public void setUserType(String userType){this.userType = userType;}

    /**
     * Function to create a JSON object of a UserInformation
     * JSON object is then made into a string and returned
     *
     * @return jsonString string form of JSON object UserInformation
     */
    public static String jsonStringify(String name, String email, String major, String gradDate,String userType, ArrayList<String> interests) {
        JSONObject jsonString = null;
        try {
            //Create JSONObject here
            jsonString = new JSONObject();
            jsonString.put("name", name);
            jsonString.put("email", email);
            jsonString.put("major", major);
            jsonString.put("gradDate", gradDate);
            jsonString.put("userType", userType);
            jsonString.put("Interests", interests);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG:", jsonString.toString());

        return jsonString.toString();
    }

}
