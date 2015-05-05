package servlets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.AccountManager;
import user.GameResults;
import user.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

    @SuppressWarnings("unchecked")
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        User usr = manager.getAuthenticated(session.getId());
        JSONObject answer = new JSONObject();

        if (usr != null) {
            JSONArray results = new JSONArray();
            if (usr.getGameResults() != null) {
                for(GameResults result: usr.getGameResults()) {
                    JSONObject resultInJSON = new JSONObject();
                    resultInJSON.put("user1", result.getUser1().getUsername());
                    resultInJSON.put("score1", result.getUser1Score());
                    resultInJSON.put("user2", result.getUser2().getUsername());
                    resultInJSON.put("score2", result.getUser2Score());
                    results.add(resultInJSON);
                }
            }

            answer.put("loggedIn", true);
            answer.put("username", usr.getUsername());
            answer.put("email",    usr.getEmail());
            answer.put("score",    usr.getScore());
            answer.put("results",  results);
        } else {
            answer.put("loggedIn", false);
            answer.put("username", "Guest");
            answer.put("email",    "none");
            answer.put("score",    0);
            answer.put("results", null);
        }

        response.getWriter().write(answer.toJSONString());
    }
}
