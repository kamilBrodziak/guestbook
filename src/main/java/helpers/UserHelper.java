package helpers;

import com.sun.net.httpserver.HttpExchange;
import controller.CookieController;
import dao.LoginDAO;
import dao.SessionDAO;
import model.Login;

import java.sql.SQLException;

public class UserHelper {
    private LoginDAO loginDAO;
    private SessionDAO sessionDAO;
    private CookieController cookieController;

    public UserHelper(LoginDAO loginDAO, SessionDAO sessionDAO, CookieController cookieController) {
        this.loginDAO = loginDAO;
        this.sessionDAO = sessionDAO;
        this.cookieController = cookieController;
    }

    public boolean isLogged(HttpExchange httpExchange) throws SQLException {
        return !cookieController.isNewSession(httpExchange) &&
                sessionDAO.getSessionByCookie(
                        cookieController.getSessionIdCookie(httpExchange).get().getValue()).isPresent();
    }

    public Login getCurrentLogged(HttpExchange httpExchange) throws SQLException {
        return loginDAO.getLoginByID(
                sessionDAO.getSessionByCookie(
                        cookieController.getSessionIdCookie(httpExchange).get().getValue()).get().getLoginID()).get();
    }
}
