package servlets;

import org.json.JSONObject;
import resources.AccountManagerResource;
import resources.ResourceProvider;
import user.AccountManager;

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
public class LoginServlet extends HttpServlet {
    private final AccountManager manager;

    public LoginServlet(AccountManager mgr) {
        manager = mgr;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        manager.logout(session.getId());

        AccountManagerResource resource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");

        String username = request.getParameter(resource.getUsernameAPIName());
        String password = request.getParameter(resource.getPasswordAPIName());

        JSONObject result = manager.authenticate(session.getId(), username, password);
        response.getWriter().write(result.toString());
    }
}
