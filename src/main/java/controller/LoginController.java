package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.*;
import helpers.FormParser;
import helpers.TwigLoader;
import model.Login;
import service.LoginService;
import service.RegisterService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class LoginController implements HttpHandler {
    private LoginService loginService;
    private RegisterService registerService;

    public LoginController() {
        DBConnector dbConnector = new DBConnector();
        LoginDAO loginDAO = new LoginDAOPostgreSQL(dbConnector);
        SessionDAO sessionDAO = new SessionDAOPostgreSQL(dbConnector);
        CookieController cookieController = new CookieController();
        TwigLoader twigLoader = new TwigLoader();
        this.loginService = new LoginService(loginDAO, sessionDAO, cookieController, twigLoader);
        this.registerService = new RegisterService(loginDAO, sessionDAO, cookieController, twigLoader);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            if(isSubmitted(httpExchange, method)) {
                Map<String, String> inputs = FormParser.parseFormData(httpExchange);
                Login login = new Login(0, inputs.get("login"), inputs.get("password"));
                if (inputs.get("logReg").equals("login")) {
                    loginService.login(httpExchange, login);
                } else {
                    registerService.register(httpExchange, login);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isSubmitted(HttpExchange httpExchange, String method) throws IOException, SQLException {
        if(method.equals("GET")){
            loginService.loadPage(httpExchange);
            return false;
        }

        return true;
    }
}
