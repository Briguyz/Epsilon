package com.example.cs4532.umdalive.Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity {
    /**
     * used to get the current user
     *
     * @param userData description of user
     * @return user's UserInfoModel
     */
    public static UserInformationModel getUser(String userData) {
        try {
            JSONObject user = new JSONObject(userData);
            ArrayList<ClubInformationModel> list = new ArrayList<>();
            JSONArray jArray = user.getJSONArray("clubs");

            if (jArray != null) {
                int len = jArray.length();
                for (int i = 0; i < len; i++) {
                    JSONObject clubObject = jArray.getJSONObject(i);
                    //create new club object from server data
                    ClubInformationModel tempClub = new ClubInformationModel(clubObject.get("clubName").toString(),
                            clubObject.get("username").toString(), clubObject.get("keywords").toString(), clubObject.get("ownerEmail").toString(),
                            clubObject.get("description").toString());

                    Log.d("club name: ", clubObject.get("clubName").toString());
                    //add new club object to array
                    list.add(tempClub);
                }
            }
            Log.d("userData", userData);
            //will obtain json string from textView and take value out from string
            return new UserInformationModel(user.getString("name"), user.getString("major"),
                    user.getString("email"), user.getString("graduationDate"), user.getString("userType"), list);
        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonString representing all posts
     * @return ArrayList of PostInfoModels
     */
    public static ArrayList<PostInformationModel> refreshPosts(String jsonString) {
        ArrayList<PostInformationModel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = len - 1; i >= 0; i--) {
                    JSONObject currentJSON = jsonArray.getJSONObject(i);
                    list.add(new PostInformationModel(currentJSON));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
