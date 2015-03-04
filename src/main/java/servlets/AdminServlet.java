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
 * Created by mihanik on 04.03.15.
 */
public class AdminServlet extends HttpServlet {
    private TemplateGenerator tg = new TemplateGenerator();
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("userID");

        User usr = MapAccountManager.getManager().getAuthenticated(uid);
        if(usr.getStatus() == User.Rights.ADMIN) {
            pageVariables.put("status", "OK");
            Map<String,User> m = MapAccountManager.getManager().getAllRegistered();
            pageVariables.put("users", m);
        }
        tg.generate(response.getWriter(), "admin.json", pageVariables);
    }
}
