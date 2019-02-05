package service;

import com.sun.net.httpserver.HttpExchange;
import dao.LoginDAO;
import dao.SessionDAO;
import helpers.CookieCreator;
import helpers.TwigLoader;
import helpers.UserHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class GuestBookService {
    private LoginDAO loginDAO;
    private SessionDAO sessionDAO;
    private CookieCreator cookieCreator;
    private TwigLoader twigLoader;
    private UserHelper userHelper;

    public GuestBookService(LoginDAO loginDAO, SessionDAO sessionDAO, CookieCreator cookieCreator, TwigLoader twigLoader) {
        this.loginDAO = loginDAO;
        this.sessionDAO = sessionDAO;
        this.cookieCreator = cookieCreator;
        this.twigLoader = twigLoader;
        userHelper = new UserHelper(loginDAO, sessionDAO, cookieCreator);
    }

    public void loadPage(HttpExchange httpExchange) throws SQLException, IOException {
        Map<String, Object> twigAttrMap = new HashMap<>();
        if(userHelper.isLogged(httpExchange)) {
            twigAttrMap.put("commentInfo", "hideInfo");
        } else {
            twigAttrMap.put("commentInput", "readonly");
            twigAttrMap.put("commentButton", "disabled");
            twigAttrMap.put("commentInfo", "displayInfo");
        }
        String response = twigLoader.loadTemplate(httpExchange, "guestbook", twigAttrMap);
        twigLoader.sendResponse(httpExchange, response);
    }

    public boolean isLogged(HttpExchange httpExchange) throws SQLException {
        return userHelper.isLogged(httpExchange);
    }
}
