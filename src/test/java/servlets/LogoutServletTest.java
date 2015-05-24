package servlets;

import org.junit.Before;
import org.junit.Test;
import resources.ResourceProvider;
import resources.ServerPathResource;
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
    private static String url;
    private static final AccountManager mgr = new MapAccountManager();
    private final LogoutServlet logoutPage = new LogoutServlet(mgr);
    HttpSession session;
    HttpServletRequest request;
    HttpServletResponse response;

    final String username = "username";
    final String password = "pwd";
    final String email    = "some_email";

    HttpServletRequest getMockRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        when(request.getPathInfo()).thenReturn(url);
        return request;
    }

    @Before
    public void initialize() {
        ServerPathResource srvpResource = (ServerPathResource) ResourceProvider.getProvider().getResource("server_path.xml");
        url = srvpResource.getLogoutUrl();

        session = mock(HttpSession.class);
        when(session.getId()).thenReturn("sessionId");

        request = getMockRequest();
        when(request.getSession()).thenReturn(session);

        response = mock(HttpServletResponse.class);

        mgr.registerUser(username, password, email);
        mgr.authenticate(session.getId(), username, password);
    }

    @Test
    public void testLogout() throws Exception {
        Integer beforeCount = mgr.getSessionCount();

        logoutPage.doPost(request, response);
        assertEquals("Expected less sessions than before logout", new Integer(beforeCount-1), mgr.getSessionCount());

        beforeCount = mgr.getSessionCount();
        logoutPage.doPost(request, response);
        assertEquals("Expected as much sessions as before logout", beforeCount, mgr.getSessionCount());
    }

    @Test
    public void testRedirect() throws Exception {
        logoutPage.doPost(request, response);
        verify(response, atLeastOnce()).sendRedirect("/");
    }

    @Test
    public void testNoLogout() throws Exception {
        Integer beforeCount = mgr.getSessionCount();

        when(session.getId()).thenReturn("sessionId_wrong");
        logoutPage.doPost(request, response);
        assertEquals("Expected as much sessions as before logout", beforeCount, mgr.getSessionCount());
    }
}