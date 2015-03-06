package servlets;

import org.eclipse.jetty.server.Server;
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
    private Server server;

    public AdminServlet(Server server){
        this.server = server;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("userID");

        User usr = MapAccountManager.getManager().getAuthenticated(uid);
        if(usr != null && usr.getStatus() == User.Rights.ADMIN) {
            pageVariables.put("status", "OK");
            Map<String, User> m = MapAccountManager.getManager().getAllRegistered();
            pageVariables.put("users", m);
            tg.generate(response.getWriter(), "admin.json", pageVariables);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("userID");

        User usr = MapAccountManager.getManager().getAuthenticated(uid);
        if(usr != null && usr.getStatus() == User.Rights.ADMIN) {
            if (request.getPathInfo() != null) {
                try {
                    server.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
