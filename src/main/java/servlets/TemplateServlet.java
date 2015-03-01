package servlets;

/**
 * Created by Михаил on 01.03.2015.
 */

import java.io.IOException;

import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TemplateServlet extends HttpServlet {
    private TemplateGenerator tg = new TemplateGenerator();
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);
        tg.generate(response.getWriter(), request.getServletPath(), pageVariables);
    }
}
