package com.example.cs4532.umdalive.fragments.base;

/**
 * Author: Henry Trinh
 * This is the class that will take in comment data
 */
public class CommentFragMaker {
    String Image;
    String UserName;
    String UserComment;
    String UserTime;



    public CommentFragMaker(String image, String name, String comment, String time) {
        Image = image;
        UserName = name;
        UserComment = comment;
        UserTime = time;


    }

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
}
