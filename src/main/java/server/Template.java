package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class Template implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // generate a lucky number
        int luckyNumber = new Random().nextInt(100);

        // create a sample hashmap
        Map<String, String> usersPass = new HashMap<>();
        usersPass.put("user", "user");
        usersPass.put("admin", "admin");
        usersPass.put("haslo", "maslo");

        List<ArrayList<String>> userPasses = new ArrayList<>();
        ArrayList<String> userPass = new ArrayList<>();
        userPass.add("test");
        userPass.add("test2");
        userPasses.add(userPass);

        // client's address
        String userAgent = httpExchange.getRequestHeaders().getFirst("User-agent");

        // get a template file
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/example.twig");

        // create a model that will be passed to a template
        JtwigModel model = JtwigModel.newModel();

        // fill the model with values
        model.with("client", userAgent);
        model.with("lucky_number", luckyNumber);
        model.with("users_pass", userPasses);

        // render a template to a string
        String response = template.render(model);

        // send the results to a the client
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}