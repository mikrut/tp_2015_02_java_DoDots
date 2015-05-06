package servlets;

import org.eclipse.jetty.server.Server;
import user.AccountManager;
import database.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mihanik
 * 04.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
public class AdminServlet extends HttpServlet {
    private final TemplateGenerator tg = new TemplateGenerator();
    private final Server server;
    private final AccountManager manager;

    public AdminServlet(Server server, AccountManager mgr){
        this.server = server;
        this.manager = mgr;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession session = request.getSession();

        User usr = manager.getAuthenticated(session.getId());

        if(usr != null && usr.getStatus() == User.Rights.ADMIN) {
            pageVariables.put("status", "OK");
            Map<String, User> m = manager.getAllRegistered();
            pageVariables.put("users", m);
            tg.generate(response.getWriter(), "admin.json", pageVariables);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) {
        HttpSession session = request.getSession();

        User usr = manager.getAuthenticated(session.getId());

        if (usr != null && usr.getStatus() == User.Rights.ADMIN) {
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
