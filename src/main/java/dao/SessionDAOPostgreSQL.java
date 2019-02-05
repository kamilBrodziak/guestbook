package dao;

import model.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SessionDAOPostgreSQL implements SessionDAO {
    private DBConnector dbConnector;

    public SessionDAOPostgreSQL(DBConnector dbConnector){
        this.dbConnector = dbConnector;
    }

    public void addSession(Session session) throws SQLException {
        String query = "INSERT INTO loginSessions (session, login_id, end_date) VALUES (?, ?::int, ?::timestamp);";
        String[] queryAttr = {session.getSession(),
                Integer.toString(session.getLoginID()), session.getDate().toString()};
        dbConnector.updateSQL(query, queryAttr);
    }

    public void deleteSessionByLoginID(int loginID) throws SQLException{
        String query = "DELETE loginSessions WHERE login_id=?;";
        String[] queryAttr = {Integer.toString(loginID)};
        dbConnector.updateSQL(query, queryAttr);
    }

    public void deleteSession(String session) throws SQLException {
        String query = "DELETE FROM loginSessions WHERE session=?;";
        String[] queryAttr = {session};
        dbConnector.updateSQL(query, queryAttr);
    }

    public Optional<Session> getSessionByCookie(String session) throws SQLException {
        String query = "SELECT * FROM loginSessions WHERE session=?;";
        String[] queryAttr = {session};
        ResultSet rs = dbConnector.executeSelect(query, queryAttr);
        if(rs.next()) {
            return Optional.of(new Session(rs.getString("session"), rs.getInt("login_id"),
                    rs.getTimestamp("end_date")));
        }
        return Optional.ofNullable(null);
    }

    public int getSessionCount() throws SQLException {
        String query = "SELECT COUNT(*) count FROM loginSessions";
        String[] queryAttr = {};
        ResultSet rs = dbConnector.executeSelect(query, queryAttr);
        if(rs.next()) {
            rs.getInt("count");
        }
        return 0;
    }
}
