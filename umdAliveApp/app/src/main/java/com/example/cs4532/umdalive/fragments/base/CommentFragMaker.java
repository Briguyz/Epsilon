package com.example.cs4532.umdalive.fragments.base;

/**
 * @author : Henry Trinh
 * This is the class that will take in single comment data
 * 
 */
public class CommentFragMaker {
    String Image;
    String UserName;
    String UserComment;
    String UserTime;
    String CommentID;
    String UserID;



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

}
