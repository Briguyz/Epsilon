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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.create.CreateReportFrag;

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
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, @SuppressLint("RecyclerView") final int position) {
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

        holder.hideCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.userProfile.getVisibility() == View.VISIBLE) {
                    holder.userProfile.setVisibility(View.GONE);
                    holder.commentName.setVisibility(View.GONE);
                    holder.commentString.setVisibility(View.GONE);
                    holder.commentTime.setVisibility(View.GONE);
                } else {
                    holder.userProfile.setVisibility(View.VISIBLE);
                    holder.commentName.setVisibility(View.VISIBLE);
                    holder.commentString.setVisibility(View.VISIBLE);
                    holder.commentTime.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.reportCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                CreateReportFrag frag = new CreateReportFrag();
                Bundle data = new Bundle();
                frag.setArguments(data);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
                Toast.makeText(v.getContext(), "Moving to Report Page", Toast.LENGTH_LONG).show();
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
        ImageButton hideCommentButton , reportCommentButton;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.imageView4);
            commentName = itemView.findViewById(R.id.comment_UserName);
            commentString = itemView.findViewById(R.id.comment_UserComment);
            commentTime = itemView.findViewById(R.id.comment_UserTime);
            hideCommentButton = itemView.findViewById(R.id.hideButton);
            reportCommentButton = itemView.findViewById(R.id.reportButton);
        }
    }
}
