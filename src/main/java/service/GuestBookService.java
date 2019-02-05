package service;

import com.sun.net.httpserver.HttpExchange;
import dao.CommentDAO;
import dao.LoginDAO;
import dao.SessionDAO;
import controller.CookieController;
import helpers.TwigLoader;
import helpers.UserHelper;
import model.Comment;
import model.Login;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuestBookService {
    private LoginDAO loginDAO;
    private SessionDAO sessionDAO;
    private CommentDAO commentDA0;
    private CookieController cookieController;
    private TwigLoader twigLoader;
    private UserHelper userHelper;

    public GuestBookService(LoginDAO loginDAO, SessionDAO sessionDAO,
                            CommentDAO commentDAO, CookieController cookieController, TwigLoader twigLoader) {
        this.loginDAO = loginDAO;
        this.sessionDAO = sessionDAO;
        this.cookieController = cookieController;
        this.twigLoader = twigLoader;
        this.commentDA0 = commentDAO;
        userHelper = new UserHelper(loginDAO, sessionDAO, cookieController);
    }

    public void loadPage(HttpExchange httpExchange) throws SQLException, IOException {
        Map<String, Object> twigAttrMap = new HashMap<>();
        List<Comment> commentList = commentDA0.getCommentList();
        twigAttrMap.put("commentList", commentList);


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

    public void addComment(HttpExchange httpExchange, String commentText) throws SQLException, IOException {
        Login login = userHelper.getCurrentLogged(httpExchange);
        Comment comment = new Comment(0, login.getLogin(), commentText, new Timestamp(System.currentTimeMillis()));
        commentDA0.addComment(comment);
        loadPage(httpExchange);
    }

}
