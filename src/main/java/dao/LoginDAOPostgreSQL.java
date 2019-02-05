package dao;


import model.Login;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class LoginDAOPostgreSQL implements LoginDAO {
    private DBConnector dbConnector;

    public LoginDAOPostgreSQL(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public Optional<Login> getLoginByID(int id) throws SQLException {
        String query = "SELECT * FROM logins WHERE id=?::integer;";
        String[] args = {Integer.toString(id)};
        return getLogin(query, args);
    }

    @Override
    public Optional<Login> getLoginByLoginName(String login) throws SQLException {
        String query = "SELECT * FROM logins WHERE login=?;";
        String[] attr = {login};
        return getLogin(query, attr);
    }

    private Optional<Login> getLogin(String query, String[] args) throws SQLException{
        ResultSet rs = dbConnector.executeSelect(query, args);
        if(rs.next()) {
            return Optional.of(new Login(rs.getInt("id"),
                    rs.getString("login"), rs.getString("password")));
        }
        return Optional.ofNullable(null);
    }

    @Override
    public void addLogin(Login login) throws SQLException {
        String query = "INSERT INTO logins(login, password) VALUES(?, ?);";
        String[] attr = {login.getLogin(), login.getPassword()};
        dbConnector.updateSQL(query, attr);
    }

    @Override
    public void updateLogin(Login login) throws SQLException {
        String query = "UPDATE logins SET password=? WHERE id=?::integer;";
        String[] attr = {login.getPassword(), Integer.toString(login.getId())};
        dbConnector.updateSQL(query, attr);

    }

    @Override
    public void deleteLoginByID(int id) throws SQLException {
        String query = "DELETE logins WHERE id=?::integer;";
        String[] attr = {Integer.toString(id)};
        dbConnector.updateSQL(query, attr);
    }
}
