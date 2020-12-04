package com.example.cs4532.umdalive.fragments.base;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : Henry Trinh
 * This is the class that will take in single comment data
 * 
 */
public class CommentFragMaker implements Comparable<CommentFragMaker>{
    String Image;
    String UserName;
    String UserComment;
    String UserTime;
    String CommentID;
    String UserID;

    //This is for the sort functions to have temp variables for sorting
    Date TempDate;
    Date Temp2Date;


    public CommentFragMaker(String image, String name, String comment, String time, String commentID, String userID) {
        Image = image;
        UserName = name;
        UserComment = comment;
        UserTime = time;
        CommentID = commentID;
        UserID = userID;


    }
    //Getters and Setters for the parameters needed for CommentsViewFrag and CommentFragAdapter
    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserComment() {
        return UserComment;
    }

    public void setUserComment(String userComment) {
        UserComment = userComment;
    }

    public String getUserTime() {
        return UserTime;
    }

    public void setUserTime(String userTime) {
        UserTime = userTime;
    }

    public void setCommentID(String commentID) { CommentID = commentID; }

    public String getCommentID() { return CommentID; }

    public String getUserID() { return UserID; }

    public void setUserID(String userID) { UserID = userID; }

    public Date getTempDate() {
        return TempDate;
    }

    public Date getTemp2Date() {
        return Temp2Date;
    }

    /**
     * @author Henry Trinh
     * @param o object coming in
     * @return int
     *
     * This function will compareTo entries one coming in and the current object
     * Then returns an int depending on the condition
     */
    @Override
    public int compareTo(@NonNull CommentFragMaker o) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");
        try {
            TempDate = formatter.parse(getUserTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Temp2Date = formatter.parse(o.getUserTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return TempDate.compareTo(Temp2Date);
    }
}

