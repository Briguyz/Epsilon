package com.example.cs4532.umdalive.fragments.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.UserSingleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if (UserSingleton.getInstance().getProfileUrl() != null) {
            Glide.with(mContext)
                    .load(UserSingleton.getInstance().getProfileUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.userProfile);
        } else {
            Glide.with(mContext)
                    .load("https://images.homedepot-static.com/productImages/42613c1a-7427-4557-ada8-ba2a17cca381/svn/gorilla-carts-yard-carts-gormp-12-64_1000.jpg")
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.userProfile);
        }

        holder.commentName.setText(mData.get(position).getUserName());
        holder.commentString.setText(mData.get(position).getUserComment());
        holder.commentTime.setText(mData.get(position).getUserTime());
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
            commentString.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    /**
     *  What will be displayed with each comment
     * @param
     * @return strDate
     */
    private String getCurrentCommentTime() {
        //Time parameter with calender, date import
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
        String strDate = dateFormat.format(currentTime);

        return strDate;
    }
}
