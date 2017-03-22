package com.example.kevin.umdalive;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Andrew Miller on 3/15/2017.
 *
 * Rest Class to perform all server interaction
 *
 * communication is done from view to presenter to RestModel.
 */

public class RestModel {
    public final String serverAddress = "http://10.0.2.2:5000"; //Emulator Tunnel
    //public final String serverAddress = "https://lempo.d.umn.edu:5001"; //To be used for a real address

    private Context context;
    /**
     * The context might be used later debugging with toast messages. Right now it is not needed though.
     * @param context for toast
     */
    public RestModel(Context context){
        this.context = context;
    }

    /**
     * Constructor for RestModel
     */
    public RestModel(){}

    public void setContext(Context context){
        this.context = context;
    }

    public String restGet(String getString, String data){
        switch(getString){
            case "getAllClubs": return getAllClubs();
            case "getClub": return getClub(data);
            case "getRecentPosts": return getRecentPosts();
            case "getUserData": return getUserData();
            default: return null;
        }
    }

    public String restPost(String postString, String data){
        return null;
    }

    public String restPut(String putString, String data){
        switch(putString){
            case "putNewClub": putNewClub(data);
                break;
            case "putNewPost": putNewPost(data);
                break;
            case "putNewUser": putNewUser(data);
                break;
            default: break;
        }
        return null;
    }

    public String restDelete(String deleteString, String data){
        return null;
    }

    /**
     * Taken from AllClubs.java
     * Not really sure why they sent the jsonParam through, look into that.
     *
     * @return String of JSON response
     */
    private String getClub(String data){
        try{
            return new HTTPAsyncTask().execute(serverAddress + "/getAllClubs", "GET", data).get();
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAllClubs(){
        try{
            return new HTTPAsyncTask().execute(serverAddress + "/getAllClubs", "GET").get();
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * From MainActivity refreshPosts function.
     *
     * @return JSON data of recent posts
     */
    private String getRecentPosts(){
        String mostRecentPosts = null;
        try {
            mostRecentPosts = new HTTPAsyncTask().execute(serverAddress + "/mostRecentPosts", "GET").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return mostRecentPosts;
    }

    /**
     * From MainActivity
     * @return JSON data of user info
     */
    private String getUserData(){
        String userData;
        try {
            userData = new HTTPAsyncTask().execute(serverAddress + "/userDataGet", "GET").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            userData = null;
        }
        return userData;
    }

    /**
     * Taken from Club.java
     * @param data name of the club being fetched
     */
    private void putNewClub(String data) {
        new HTTPAsyncTask().execute(serverAddress + "/newClub", "PUT", data);
    }

    /**
     * Taken from PostingActivity
     * @param data the post to be made
     */
    private void putNewPost(String data){
        new HTTPAsyncTask().execute(serverAddress + "/newPost", "PUT", data);
    }




    private void putNewUser(String data){
        new HTTPAsyncTask().execute(serverAddress + "/userInformation", "PUT", data);
    }

    /**
     * The previous group just copy and pasted all this. At some point we make it better.
     */
    private class HTTPAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection serverConnection = null;
            InputStream is;

            Log.d("Debug:", "Attempting to connect to: " + params[0]);

            try {
                URL url = new URL(params[0]);
                serverConnection = (HttpURLConnection) url.openConnection();
                serverConnection.setRequestMethod(params[1]);
                if (params[1].equals("POST") || params[1].equals("PUT") || params[1].equals("DELETE")) {
                    Log.d("DEBUG POST/PUT/DELETE:", "In post: params[0]=" + params[0] + ", params[1]=" + params[1] + ", params[2]=" + params[2]);
                    serverConnection.setDoOutput(true);
                    serverConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    // params[2] contains the JSON String to send, make sure we send the
                    // content length to be the json string length
                    serverConnection.setRequestProperty("Content-Length", "" +
                            Integer.toString(params[2].getBytes().length));
                    // Send POST data that was provided.
                    DataOutputStream out = new DataOutputStream(serverConnection.getOutputStream());
                    out.writeBytes(params[2]);
                    out.flush();
                    out.close();
                }

                int responseCode = serverConnection.getResponseCode();
                Log.d("Debug:", "\nSending " + params[1] + " request to URL : " + params[0]);
                Log.d("Debug: ", "Response Code : " + responseCode);
                is = serverConnection.getInputStream();

                if (params[1].equals("GET") || params[1].equals("POST") || params[1].equals("PUT") || params[1].equals("DELETE")) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    try {
                        JSONObject jsonData = new JSONObject(sb.toString());
                        return jsonData.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert serverConnection != null;
                serverConnection.disconnect();
            }
            return "Should not get to this if the data has been sent/received correctly!";
        }

        /**
         * @param result the result from the query
         */
        protected void onPostExecute(String result) {
            Log.d("onPostExecute JSON: ", result);
            //Toast.makeText(context, "Data transfer successful", Toast.LENGTH_SHORT).show();
        }
    }

    public String get

}