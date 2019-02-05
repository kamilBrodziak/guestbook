package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.*;
import helpers.CookieCreator;
import helpers.TwigLoader;
import service.GuestBookService;

import java.io.IOException;
import java.sql.SQLException;

public class GuestBookController implements HttpHandler {
    private GuestBookService guestBookService;

    public GuestBookController() {
        DBConnector dbConnector = new DBConnector();
        LoginDAO loginDAO = new LoginDAOPostgreSQL(dbConnector);
        SessionDAO sessionDAO = new SessionDAOPostgreSQL(dbConnector);
        CookieCreator cookieCreator = new CookieCreator();
        TwigLoader twigLoader = new TwigLoader();
        guestBookService = new GuestBookService(loginDAO, sessionDAO, cookieCreator, twigLoader);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            if(isSubmitted(httpExchange, method)) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isSubmitted(HttpExchange httpExchange, String method) throws IOException, SQLException {
        if(method.equals("GET")){
            guestBookService.loadPage(httpExchange);
        }
        return true;
    }
}
