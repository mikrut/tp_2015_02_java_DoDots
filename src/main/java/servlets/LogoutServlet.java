package servlets;

import user.MapAccountManager;

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
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        MapAccountManager.getManager().logout((Long) session.getAttribute("userID"));
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.sendRedirect("/");
    }
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        MapAccountManager.getManager().logout((Long) session.getAttribute("userID"));
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.sendRedirect("/");
    }
}
