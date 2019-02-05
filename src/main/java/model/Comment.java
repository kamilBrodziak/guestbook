package model;

import java.util.Date;

public class Comment {
    private int id;
    private String login;
    private String commentText;
    private Date date;
    private String dateString;

    public Comment(int id, String login, String commentText, Date date) {
        this.id = id;
        this.login = login;
        this.commentText = commentText;
        this.date = date;
        this.dateString = date.toString().substring(0, 16);
    }

    public int getID() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getCommentText() {
        return commentText;
    }

    public Date getDate() {
        return date;
    };

    public String getDateString() { return dateString; }

}
