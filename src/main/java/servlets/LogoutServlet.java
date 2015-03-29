package servlets;

import user.AccountManager;

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
public class LogoutServlet extends HttpServlet {
    private AccountManager manager;
    public LogoutServlet(AccountManager mgr) {
        manager = mgr;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        manager.logout(session.getId());
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.sendRedirect("/");
    }

}
