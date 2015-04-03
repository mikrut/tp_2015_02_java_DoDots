package servlets;

import junit.framework.Assert;
import org.junit.Test;
import user.AccountManager;
import user.MapAccountManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeastOnce;

public class LogoutServletTest {
    final private static String url = "/logout";
    private static final AccountManager mgr = new MapAccountManager();
    private final LogoutServlet logoutPage = new LogoutServlet(mgr);

    HttpServletRequest getMockRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        when(request.getPathInfo()).thenReturn(url);
        return request;
    }

    @Test
    public void testLogout() throws Exception {
        final String username = "username";
        final String password = "pwd";
        final String email    = "some_email";
        final HttpSession session = mock(HttpSession.class);

        when(session.getId()).thenReturn("sessionid");

        HttpServletRequest request = getMockRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);

        mgr.registerUser(username, password, email);
        mgr.authenticate(session.getId(), username, password);

        Integer beforeCount = mgr.getSessionCount();
        when(request.getSession()).thenReturn(session);

        logoutPage.doPost(request, response);
        assertEquals("Expected less sessions than before logout", new Integer(beforeCount-1), mgr.getSessionCount());
        verify(response, atLeastOnce()).sendRedirect("/");

        when(session.getId()).thenReturn("sessionid_wrong");
        beforeCount = mgr.getSessionCount();
        logoutPage.doPost(request, response);
        assertEquals("Expected as much sessions as before logout", beforeCount, mgr.getSessionCount());
    }
}