package servlets;

import user.AccountManager;
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
public class UserinfoServlet extends HttpServlet {
    private AccountManager manager;
    private TemplateGenerator tg = new TemplateGenerator();

    public UserinfoServlet(AccountManager mgr) {
        manager = mgr;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        User usr = manager.getAuthenticated(session.getId());

        if (usr != null) {
            pageVariables.put("loggedIn", true);
            pageVariables.put("username", usr.getUsername());
        } else {
            pageVariables.put("loggedIn", false);
            pageVariables.put("username", "Guest");
        }

        tg.generate(response.getWriter(), "userinfo.json", pageVariables);
    }
}
