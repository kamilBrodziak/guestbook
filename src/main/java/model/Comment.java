package model;

import java.util.Date;

public class Comment {
    private int id, loginID;
    private String commentText;
    private Date date;

    public Comment(int id, int loginID, String commentText, Date date) {
        this.id = id;
        this.loginID = loginID;
        this.commentText = commentText;
        this.date = date;
    }

    public int getID() {
        return id;
    }

    public int getLoginID() {
        return loginID;
    }

    public String getCommentText() {
        return commentText;
    }

    public Date getDate() {
        return date;
    }
}
