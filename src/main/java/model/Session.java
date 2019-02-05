package model;

import java.sql.Timestamp;
import java.util.Date;

public class Session {
    private String session;
    private int loginID;
    private Date date;

    public Session(String session, int loginID, Date date) {
        this.session = session;
        this.loginID = loginID;
        this.date = date;
    }

    public String getSession() {
        return session;
    }

    public int getLoginID() {
        return loginID;
    }

    public Date getDate() {
        return date;
    }
}