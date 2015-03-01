package servlets;

import user.MapAccountManager;
import user.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Михаил on 01.03.2015.
 */
public class LoginServlet extends HttpServlet {
    private TemplateGenerator tg = new TemplateGenerator();
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession session = request.getSession();
        if((Long) session.getAttribute("userID") != null) {
            MapAccountManager.getManager().logout((Long) session.getAttribute("userID"));
            session.setAttribute("userID", null);
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User usr = null;
        try {
            usr = MapAccountManager.getManager().authenticate(username, password);
            pageVariables.put("status", "OK");
            pageVariables.put("message", "Login success");
            session.setAttribute("userID", usr.getID());
        } catch (Exception e) {
            pageVariables.put("status", "Error");
            pageVariables.put("message", "Login fail");
        }
        tg.generate(response.getWriter(), "login.json", pageVariables);
    }
}
