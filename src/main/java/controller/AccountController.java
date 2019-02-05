package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.*;
import helpers.CookieCreator;
import helpers.TwigLoader;
import service.AccountService;
import service.LoginService;
import service.RegisterService;

import java.io.IOException;
import java.sql.SQLException;

public class AccountController implements HttpHandler {
    private LoginService loginService;
    private RegisterService registerService;
    private AccountService accountService;

    public AccountController() {
        DBConnector dbConnector = new DBConnector();
        LoginDAO loginDAO = new LoginDAOPostgreSQL(dbConnector);
        SessionDAO sessionDAO = new SessionDAOPostgreSQL(dbConnector);
        CookieCreator cookieCreator = new CookieCreator();
        TwigLoader twigLoader = new TwigLoader();
        this.loginService = new LoginService(loginDAO, sessionDAO, cookieCreator, twigLoader);
        this.registerService = new RegisterService(loginDAO, sessionDAO, cookieCreator, twigLoader);
        this.accountService = new AccountService(loginDAO, sessionDAO, cookieCreator, twigLoader);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            if(isSubmitted(httpExchange, method)) {
                loginService.logout(httpExchange);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isSubmitted(HttpExchange httpExchange, String method) throws IOException, SQLException {
        if(method.equals("GET")){
            if(loginService.isLogged(httpExchange)) {
                accountService.loadAccountPage(httpExchange);
            } else {
                loginService.redirectToLogin(httpExchange);
            }
            return false;
        }

        return true;
    }
}
