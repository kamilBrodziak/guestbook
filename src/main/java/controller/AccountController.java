package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.*;
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
        CookieController cookieController = new CookieController();
        TwigLoader twigLoader = new TwigLoader();
        this.loginService = new LoginService(loginDAO, sessionDAO, cookieController, twigLoader);
        this.registerService = new RegisterService(loginDAO, sessionDAO, cookieController, twigLoader);
        this.accountService = new AccountService(loginDAO, sessionDAO, cookieController, twigLoader);
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
            accountService.loadPage(httpExchange);
            return false;
        }
        return true;
    }
}
