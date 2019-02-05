import com.sun.net.httpserver.HttpServer;
import controller.GuestBookController;
import controller.LoginController;
import server.Static;

import java.net.InetSocketAddress;


public class App {
    public static void main(String[] args) throws Exception {
        // create a server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // set routes
        server.createContext("/static", new Static());
        server.createContext("/login", new LoginController());
        server.createContext("/guestbook", new GuestBookController());

        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();
    }
}