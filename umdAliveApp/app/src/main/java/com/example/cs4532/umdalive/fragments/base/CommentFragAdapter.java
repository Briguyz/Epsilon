package com.example.cs4532.umdalive.fragments.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.UserSingleton;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author : Henry Trinh
 * This is the comment frag holder which will add to the recycler view in CommentsView
 *
 */
public class CommentFragAdapter extends RecyclerView.Adapter<CommentFragAdapter.CommentViewHolder> {

    private Context mContext;
    private ArrayList<CommentFragMaker> mData;

    public CommentFragAdapter(Context mContext, ArrayList<CommentFragMaker> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.single_comment_layout, parent, false);
        return new CommentViewHolder(row);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, @SuppressLint("RecyclerView") final int position) {
       Log.d("imageUrl", mData.get(position).getImage());
       //Got this from profile_layout
       Glide.with(mContext)
               .load(mData.get(position).getImage())
               .apply(RequestOptions.circleCropTransform())
               .into(holder.userProfile);
        
        //Set the data with the view in single_comment_layout
        holder.commentName.setText(mData.get(position).getUserName());
        holder.commentString.setText(mData.get(position).getUserComment());
        holder.commentTime.setText(mData.get(position).getUserTime());

        holder.commentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ProfileFrag frag = new ProfileFrag();
                Bundle data = new Bundle();
                data.putString("userID", mData.get(position).getUserID());
                frag.setArguments(data);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
            }
        });

        holder.commentString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                CommentsFrag frag = new CommentsFrag();
                Bundle data = new Bundle();
                data.putString("commentID", mData.get(position).getCommentID());
                frag.setArguments(data);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
            }
        });

        holder.commentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                CommentsFrag frag = new CommentsFrag();
                Bundle data = new Bundle();
                data.putString("commentID", mData.get(position).getCommentID());
                frag.setArguments(data);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
            }
        });

        holder.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ProfileFrag frag = new ProfileFrag();
                Bundle data = new Bundle();
                data.putString("userID", mData.get(position).getUserID());
                frag.setArguments(data);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfile;
        TextView commentName, commentString, commentTime;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.imageView4);
            commentName = itemView.findViewById(R.id.comment_UserName);
            commentString = itemView.findViewById(R.id.comment_UserComment);
            commentTime = itemView.findViewById(R.id.comment_UserTime);
        }
    }
}
