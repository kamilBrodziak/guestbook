package service;

import com.sun.net.httpserver.HttpExchange;
import dao.*;
import helpers.CookieCreator;
import model.Login;
import model.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public class LoginService {
    DBConnector dbConnector;
    LoginDAO loginDAO;
    SessionDAO sessionDAO;
    CookieCreator cookieCreator = new CookieCreator();

    public LoginService(DBConnector dbConnector, CookieCreator cookieCreator) {
        this.dbConnector = dbConnector;
        loginDAO = new LoginDAOPostgreSQL(dbConnector);
        sessionDAO = new SessionDAOPostgreSQL(dbConnector);
    }

    public boolean login(HttpExchange httpExchange, String login, String password) throws SQLException {
        Optional<Login> loginFromDB = loginDAO.getLoginByLoginName(login);

        if(loginFromDB.isPresent() && loginFromDB.get().getPassword().equals(password)) {
            Session session = new Session(UUID.randomUUID().toString(),
                    loginFromDB.get().getId(),
                    new Timestamp(System.currentTimeMillis()));
            cookieCreator.createNewSession(httpExchange, session.getSession());
            sessionDAO.addSession(session);

            return true;
        }
        return false;
    }

    public boolean checkIfLogged(HttpExchange httpExchange) throws SQLException {
        return !cookieCreator.isNewSession(httpExchange) &&
                sessionDAO.getSessionByCookie(
                        cookieCreator.getSessionIdCookie(httpExchange).get().getValue()).isPresent() &&
                loginDAO.getLoginByID(
                        sessionDAO.getSessionByCookie(
                                cookieCreator.getSessionIdCookie(httpExchange).get().getValue()).get().getLoginID()).isPresent();
    }

    public boolean logout(HttpExchange httpExchange) throws SQLException, IOException {


        if(checkIfLogged(httpExchange)) {
            Session session = sessionDAO.getSessionByCookie(
                    cookieCreator.getSessionIdCookie(httpExchange).get().getValue()).get();
            sessionDAO.deleteSession(session.getSession());
            cookieCreator.deleteCookie(httpExchange);
            return true;
        }
        return false;
    }

    public Login getCurrentLogged(HttpExchange httpExchange) throws SQLException {
        Login login = loginDAO.getLoginByID(
                sessionDAO.getSessionByCookie(
                        cookieCreator.getSessionIdCookie(httpExchange).get().getValue()).get().getLoginID()).get();
    }

    public boolean register(String login, String password) throws SQLException {
        if(!loginDAO.getLoginByLoginName(login).isPresent()) {
            loginDAO.addLogin(new Login(0, login, password));
            return true;
        }
        return false;
    }

    public boolean deleteAccount(HttpExchange httpExchange, String loginName) throws SQLException {
        Optional<Login> login = loginDAO.getLoginByLoginName(loginName);
        if(login.isPresent()) {
            sessionDAO.deleteSessionByLoginID(login.get().getId());
            cookieCreator.deleteCookie(httpExchange);
            loginDAO.deleteLoginByID(login.get().getId());

        }
    }


}
