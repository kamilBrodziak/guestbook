package dao;

import model.Login;

import java.sql.SQLException;
import java.util.Optional;

public interface LoginDAO {
    public Optional<Login> getLoginByID(int id) throws SQLException;
    public Optional<Login> getLoginByLoginName(String login) throws SQLException;
    public void addLogin(Login login) throws SQLException;
    public void updateLogin(Login login) throws SQLException;
    public void deleteLoginByID(int id) throws SQLException;
}
