package servlets;

import org.json.simple.JSONObject;
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
public class LoginServlet extends HttpServlet {
    private final TemplateGenerator tg = new TemplateGenerator();
    private final AccountManager manager;

    public LoginServlet(AccountManager mgr) {
        manager = mgr;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession session = request.getSession();
        manager.logout(session.getId());

        String username = request.getParameter("name");
        String password = request.getParameter("password");

        JSONObject result = manager.authenticate(session.getId(), username, password);
        response.getWriter().write(result.toJSONString());
    }
}
