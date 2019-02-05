package service;

import com.sun.net.httpserver.HttpExchange;
import dao.*;
import controller.CookieController;
import helpers.TwigLoader;
import model.Login;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegisterService {
    LoginDAO loginDAO;
    SessionDAO sessionDAO;
    CookieController cookieController;
    TwigLoader twigLoader;

    public RegisterService(LoginDAO loginDAO, SessionDAO sessionDAO, CookieController cookieController, TwigLoader twigLoader) {
        this.loginDAO = loginDAO;
        this.cookieController = cookieController;
        this.sessionDAO = sessionDAO;
        this.twigLoader = twigLoader;
    }


    public boolean register(HttpExchange httpExchange, Login login) throws SQLException, IOException {
        Map<String, Object> twigAttrMap = new HashMap<>();
        twigAttrMap.put("loginInfo", "hideInfo");
        if(!loginDAO.getLoginByLoginName(login.getLogin()).isPresent()) {
            loginDAO.addLogin(login);

            twigAttrMap.put("registerCompleteInfo", "displayInfo");
            twigAttrMap.put("registerWrongInfo", "hideInfo");
            String response = twigLoader.loadTemplate(httpExchange, "login", twigAttrMap);
            twigLoader.sendResponse(httpExchange, response);
            return true;
        }
        twigAttrMap.put("registerCompleteInfo", "hideInfo");
        twigAttrMap.put("registerWrongInfo", "displayInfo");
        String response = twigLoader.loadTemplate(httpExchange, "login", twigAttrMap);
        twigLoader.sendResponse(httpExchange, response);
        return false;
    }

    public boolean deleteAccount(HttpExchange httpExchange, String loginName) throws SQLException, IOException {
        Optional<Login> login = loginDAO.getLoginByLoginName(loginName);
        if(login.isPresent()) {
            sessionDAO.deleteSessionByLoginID(login.get().getId());
            cookieController.deleteCookie(httpExchange);
            loginDAO.deleteLoginByID(login.get().getId());
            return true;
        }
        return false;
    }
}
