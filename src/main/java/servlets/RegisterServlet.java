package servlets;

/**
 * Created by Михаил
 * 01.03.2015 9:15
 * Package: ${PACKAGE_NAME}
 */
import java.io.IOException;

import org.json.simple.JSONObject;
import resources.AccountManagerResource;
import resources.ResourceProvider;
import user.AccountManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegisterServlet extends HttpServlet {
    private final AccountManager manager;

    public RegisterServlet(AccountManager mgr) {
        manager = mgr;
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);

        AccountManagerResource resource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");

        String username = request.getParameter(resource.getUsernameAPIName());
        String password = request.getParameter(resource.getPasswordAPIName());
        String email    = request.getParameter(resource.getEmailAPIName());

        HttpSession session = request.getSession();
        JSONObject result = manager.registerUser(username, password, email, session.getId());

        response.getWriter().write(result.toJSONString());
    }
}
