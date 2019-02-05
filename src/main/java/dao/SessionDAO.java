package dao;

import model.Session;

import java.sql.SQLException;
import java.util.Optional;

public interface SessionDAO {
    public Optional<Session> getSessionByCookie(String session) throws SQLException;
    public void addSession(Session session) throws SQLException;
    public void deleteSession(String session) throws SQLException;
    public void deleteSessionByLoginID(int loginID) throws SQLException;
    public int getSessionCount() throws SQLException;
}
