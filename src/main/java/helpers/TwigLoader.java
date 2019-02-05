package helpers;

import com.sun.net.httpserver.HttpExchange;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class TwigLoader {

    public String loadTemplate(HttpExchange httpExchange, String templateName,
                             Map<String, Object> tempAttr) throws IOException{
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/" + templateName + ".twig");
        String response = template.render(createModel(tempAttr));
        return response;
    }

    private JtwigModel createModel(Map<String, Object> attr) {
        JtwigModel model = JtwigModel.newModel();
        for(String key: attr.keySet()) {
            model.with(key, attr.get(key));
        }
        return model;
    }

    public void sendResponse(HttpExchange httpExchange, String response) throws IOException{
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void redirectToPage(HttpExchange httpExchange, String page) throws IOException {
        httpExchange.getResponseHeaders().set("Location", page);
        httpExchange.sendResponseHeaders(302, page.getBytes().length);
    }
}
