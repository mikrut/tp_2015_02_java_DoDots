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
 * Created by Михаил
 * 01.03.2015 9:15
 * Package: ${PACKAGE_NAME}
 */
public class UserInfoServlet extends HttpServlet {
    private final AccountManager manager;

    public UserInfoServlet(AccountManager mgr) {
        manager = mgr;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        User usr = manager.getAuthenticated(session.getId());
        JSONObject answer = new JSONObject();

        if (usr != null) {
            answer.put("loggedIn", true);
            answer.put("username", usr.getUsername());
            answer.put("email",    usr.getEmail());
            answer.put("score",    usr.getScore());
        } else {
            answer.put("loggedIn", false);
            answer.put("username", "Guest");
            answer.put("email",    "none");
            answer.put("score",    0);
        }

        response.getWriter().write(answer.toJSONString());
    }
}
