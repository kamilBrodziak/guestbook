package service;

import com.sun.net.httpserver.HttpExchange;
import dao.LoginDAO;
import dao.SessionDAO;
import controller.CookieController;
import helpers.TwigLoader;
import helpers.UserHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private TwigLoader twigLoader;
    private SessionDAO sessionDAO;
    private LoginDAO loginDAO;
    private CookieController cookieController;
    private UserHelper userHelper;

    public AccountService(LoginDAO loginDAO, SessionDAO sessionDAO, CookieController cookieController, TwigLoader twigLoader) {
        this.twigLoader = twigLoader;
        this.sessionDAO = sessionDAO;
        this.loginDAO = loginDAO;
        this.cookieController = cookieController;
        this.userHelper = new UserHelper(loginDAO, sessionDAO, cookieController);
    }

    public void loadAccountPage(HttpExchange httpExchange) throws IOException, SQLException {
        Map<String, Object> twigAttrMap = new HashMap<>();
        String loginName = loginDAO.getLoginByID(sessionDAO.getSessionByCookie(
                cookieController.getSessionIdCookie(httpExchange).get().getValue()).get().getLoginID()).get().getLogin();
        twigAttrMap.put("login", loginName);
        String response = twigLoader.loadTemplate(httpExchange, "account", twigAttrMap);
        twigLoader.sendResponse(httpExchange, response);
    }

    public void loadPage(HttpExchange httpExchange) throws SQLException, IOException {
        if(userHelper.isLogged(httpExchange)) {
            loadAccountPage(httpExchange);
        } else {
            redirectToLogin(httpExchange);
        }
    }

    public void redirectToLogin(HttpExchange httpExchange) throws IOException {
        twigLoader.redirectToPage(httpExchange, "login");
    }
}
