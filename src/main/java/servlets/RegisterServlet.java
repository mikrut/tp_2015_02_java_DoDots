package servlets;

/**
 * Created by Михаил on 01.03.2015.
 */

import java.io.IOException;

import user.MapAccountManager;
import user.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class RegisterServlet extends HttpServlet {
    private TemplateGenerator tg = new TemplateGenerator();
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        try {
            User user = MapAccountManager.getManager().registerUser(username, password);
            MapAccountManager.getManager().addSession(session.getId(), user);
            pageVariables.put("status", "OK");
            pageVariables.put("message", "Registration complete");
        } catch (Exception e) {
            pageVariables.put("status", "Error");
            pageVariables.put("message", e.getMessage());
        }

        tg.generate(response.getWriter(), "reg.json", pageVariables);
    }
}
