package helpers;

import com.sun.net.httpserver.HttpExchange;
import dao.LoginDAO;
import dao.SessionDAO;
import model.Login;

import java.sql.SQLException;

public class UserHelper {
    private LoginDAO loginDAO;
    private SessionDAO sessionDAO;
    private CookieCreator cookieCreator;

    public UserHelper(LoginDAO loginDAO, SessionDAO sessionDAO, CookieCreator cookieCreator) {
        this.loginDAO = loginDAO;
        this.sessionDAO = sessionDAO;
        this.cookieCreator = cookieCreator;
    }

    public boolean isLogged(HttpExchange httpExchange) throws SQLException {
        return !cookieCreator.isNewSession(httpExchange) &&
                sessionDAO.getSessionByCookie(
                        cookieCreator.getSessionIdCookie(httpExchange).get().getValue()).isPresent();
    }

    public Login getCurrentLogged(HttpExchange httpExchange) throws SQLException {
        return loginDAO.getLoginByID(
                sessionDAO.getSessionByCookie(
                        cookieCreator.getSessionIdCookie(httpExchange).get().getValue()).get().getLoginID()).get();
    }
}
