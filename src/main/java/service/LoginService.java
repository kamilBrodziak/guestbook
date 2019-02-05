package service;

import com.sun.net.httpserver.HttpExchange;
import dao.*;
import controller.CookieController;
import helpers.TwigLoader;
import helpers.UserHelper;
import model.Login;
import model.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LoginService {
    private LoginDAO loginDAO;
    private SessionDAO sessionDAO;
    private CookieController cookieController;
    private TwigLoader twigLoader;
    private UserHelper userHelper;

    public LoginService(LoginDAO loginDAO, SessionDAO sessionDAO, CookieController cookieController, TwigLoader twigLoader) {
        this.loginDAO = loginDAO;
        this.sessionDAO = sessionDAO;
        this.cookieController = cookieController;
        this.twigLoader = twigLoader;
        this.userHelper = new UserHelper(loginDAO, sessionDAO, cookieController);
    }

    public boolean login(HttpExchange httpExchange, Login login) throws SQLException, IOException {
        Optional<Login> loginFromDB = loginDAO.getLoginByLoginName(login.getLogin());

        if(loginFromDB.isPresent() && loginFromDB.get().getPassword().equals(login.getPassword())) {
            Session session = new Session(UUID.randomUUID().toString(),
                    loginFromDB.get().getId(),
                    new Timestamp(System.currentTimeMillis()));
            cookieController.createNewSession(httpExchange, session.getSession());
            sessionDAO.addSession(session);
            twigLoader.redirectToPage(httpExchange, "account");
            return true;
        }
        loadLoginPage(httpExchange, "displayInfo");
        return false;
    }

    public boolean isLogged(HttpExchange httpExchange) throws SQLException {
        return userHelper.isLogged(httpExchange);
    }

    public boolean logout(HttpExchange httpExchange) throws SQLException, IOException {
        if(isLogged(httpExchange)) {
            Session session = sessionDAO.getSessionByCookie(
                    cookieController.getSessionIdCookie(httpExchange).get().getValue()).get();
            sessionDAO.deleteSession(session.getSession());
            cookieController.deleteCookie(httpExchange);
            twigLoader.redirectToPage(httpExchange, "login");
            return true;
        }
        return false;
    }

    public void redirectToAccount(HttpExchange httpExchange) throws IOException {
        twigLoader.redirectToPage(httpExchange, "account");
    }

    public void loadLoginPage(HttpExchange httpExchange, String twigAttr) throws IOException {
        Map<String, Object> twigAttrMap = new HashMap<>();
        twigAttrMap.put("loginInfo", twigAttr);
        twigAttrMap.put("registerWrongInfo", "hideInfo");
        twigAttrMap.put("registerCompleteInfo", "hideInfo");
        twigAttrMap.put("accountNav", "Login");
        String response = twigLoader.loadTemplate(httpExchange, "login", twigAttrMap);
        twigLoader.sendResponse(httpExchange, response);
    }

    public void loadPage(HttpExchange httpExchange) throws SQLException, IOException {
        if(isLogged(httpExchange)) {
            redirectToAccount(httpExchange);
        } else {
            loadLoginPage(httpExchange, "hideInfo");
        }
    }
}
