package com.example.kevin.umdalive;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity {

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
                    ClubInformationModel tempClub = new ClubInformationModel(clubObject.get("clubname").toString(),
                            clubObject.get("username").toString(), clubObject.get("keywords").toString(),
                            clubObject.get("description").toString());

                    Log.d("club name: ", clubObject.get("clubname").toString());
                    //add new club object to array
                    list.add(tempClub);
                }
            }
            Log.d("userData", userData);
            //will obtain json string from textview and take value out from string
            return new UserInformationModel(user.getString("name"), user.getString("major"),
                    user.getString("email"), user.getString("graduationDate"), list);
        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> refreshPosts(String jsonString) {
        ArrayList<String> list = new ArrayList<>();
        //converting Json string to ArrayList
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray jsonArray = object.getJSONArray("items");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    list.add(jsonArray.get(i).toString());
                    Log.d(jsonArray.get(i).toString(), jsonArray.get(i).toString());
                }
                Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}

class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<String> postData;

    public PostAdapter(ArrayList<String> postData) {
        this.postData = postData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(TextView tView) {
            super(tView);
            textView = tView;
        }
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView tView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view, parent, false);
        // set the TextView's settings here
        tView.setTextSize(15);
        ViewHolder viewHolder = new ViewHolder(tView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(postData.get(position));
    }

    @Override
    public int getItemCount() {
        return postData.size();
    }
}



