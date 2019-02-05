package service;

import com.sun.net.httpserver.HttpExchange;
import dao.LoginDAO;
import dao.SessionDAO;
import helpers.CookieCreator;
import helpers.TwigLoader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private TwigLoader twigLoader;
    private SessionDAO sessionDAO;
    private LoginDAO loginDAO;
    private CookieCreator cookieCreator;

    public AccountService(LoginDAO loginDAO, SessionDAO sessionDAO, CookieCreator cookieCreator, TwigLoader twigLoader) {
        this.twigLoader = twigLoader;
        this.sessionDAO = sessionDAO;
        this.loginDAO = loginDAO;
        this.cookieCreator = cookieCreator;
    }

    public void loadAccountPage(HttpExchange httpExchange) throws IOException, SQLException {
        Map<String, Object> twigAttrMap = new HashMap<>();
        String loginName = loginDAO.getLoginByID(sessionDAO.getSessionByCookie(
                cookieCreator.getSessionIdCookie(httpExchange).get().getValue()).get().getLoginID()).get().getLogin();
        twigAttrMap.put("login", loginName);
        String response = twigLoader.loadTemplate(httpExchange, "account", twigAttrMap);
        twigLoader.sendResponse(httpExchange, response);
    }
}
