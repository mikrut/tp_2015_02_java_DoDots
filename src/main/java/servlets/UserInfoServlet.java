package servlets;

import org.json.JSONArray;
import org.json.JSONObject;
import resources.AccountManagerResource;
import resources.ResourceProvider;
import user.AccountManager;
import database.GameResults;
import database.User;

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
    private static final AccountManagerResource resource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");

    public UserInfoServlet(AccountManager mgr) {
        manager = mgr;
    }

    @SuppressWarnings("unchecked")
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        User usr = manager.getAuthenticated(session.getId());
        JSONObject answer = new JSONObject();

        String username = resource.getGuestName();
        String email = resource.getGuestEmail();
        Integer score = 0;
        JSONArray results = null;

        boolean loggedIn = false;
        boolean isAdmin  = false;


        if (usr != null) {
            results = new JSONArray();
            if (usr.getGameResults() != null) {
                for(GameResults result: usr.getGameResults()) {
                    JSONObject resultInJSON = new JSONObject();

                    resultInJSON.put(resource.getUser1APIName(), result.getUser1().getUsername());
                    resultInJSON.put(resource.getUser1ScoreAPIName(), result.getUser1Score());

                    resultInJSON.put(resource.getUser2APIName(), result.getUser2().getUsername());
                    resultInJSON.put(resource.getUser2ScoreAPIName(), result.getUser2Score());

                    results.put(resultInJSON);
                }
            }

            username = usr.getUsername();
            email = usr.getEmail();
            score = usr.getScore();
            loggedIn = true;
            isAdmin = usr.getStatus() == User.Rights.ADMIN;
        }

        answer.put(resource.getLoggedInAPIName(), loggedIn);
        answer.put(resource.getUsernameAPIName(), username);
        answer.put(resource.getEmailAPIName(),    email);
        answer.put(resource.getScoreAPIName(),    score);
        answer.put(resource.getResultsAPIName(),  results);
        answer.put(resource.getIsAdminName(),     isAdmin);

        response.getWriter().write(answer.toString());
    }
}
