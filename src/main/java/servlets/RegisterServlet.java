package servlets;

/**
 * Created by Михаил
 * 01.03.2015 9:15
 * Package: ${PACKAGE_NAME}
 */
import java.io.IOException;

import user.AccountManager;
import user.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class RegisterServlet extends HttpServlet {
    private final AccountManager manager;

    private final TemplateGenerator tg = new TemplateGenerator();

    public RegisterServlet(AccountManager mgr) {
        manager = mgr;
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);

        String username = request.getParameter("name");
        String password = request.getParameter("password");
        String email    = request.getParameter("email");


        HttpSession session = request.getSession();

        try {
            User user = manager.registerUser(username, password, email);
            manager.addSession(session.getId(), user);
            pageVariables.put("status", "OK");
            pageVariables.put("message", "Registration complete");
        } catch (Exception e) {
            pageVariables.put("status", "Error");
            pageVariables.put("message", e.getMessage());
        }

        tg.generate(response.getWriter(), "reg.json", pageVariables);
    }
}
