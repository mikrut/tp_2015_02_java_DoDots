package servlets;

import org.json.JSONObject;
import org.junit.Test;
import user.AccountManager;
import user.MapAccountManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserInfoServletTest {
    final private static String url = "/getinfo";
    private static final AccountManager mgr = new MapAccountManager();
    private final UserInfoServlet infoPage = new UserInfoServlet(mgr);

    HttpServletRequest getMockRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        when(request.getPathInfo()).thenReturn(url);
        return request;
    }

    HttpServletResponse getMockResponse(StringWriter stringWrite) throws Exception {
        PrintWriter writer = new PrintWriter(stringWrite);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    @Test
    public void testLogin() throws Exception{
        final StringWriter writer = new StringWriter();
        final String username = "username";
        final String password = "pwd";
        String em = "email@mail";
        final HttpSession session = mock(HttpSession.class);

        HttpServletRequest request = getMockRequest();
        HttpServletResponse response = getMockResponse(writer);

        when(session.getId()).thenReturn("dummysessionid");
        when(request.getParameter("name")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getSession()).thenReturn(session);

        mgr.registerUser(username, password, em);
        mgr.authenticate(session.getId(), username, password);

        infoPage.doGet(request, response);
        validateResponse(writer.toString(), username, true);
        writer.getBuffer().setLength(0);

        when(session.getId()).thenReturn("dummysessionid_wrong");

        infoPage.doGet(request, response);
        validateResponse(writer.toString(), "Guest", false);
        writer.getBuffer().setLength(0);

    }

    void validateResponse(String response, String username, Boolean isLoggeIn) {
        JSONObject obj = new JSONObject(response);
        if(obj != null) {
            if(obj.has("username") && obj.has("loggedIn")) {
                assertEquals("Expected \""+username+"\" username", username, obj.get("username"));
                assertEquals("Expected \""+isLoggeIn.toString()+"\" loggedIn", isLoggeIn, obj.get("loggedIn"));
            } else {
                assertTrue("Expected response to contain username string. Got only: "+response, obj.has("username"));
                assertTrue("Expected response to contain loggedIn string. Got only: "+response, obj.has("loggedIn"));
            }
        } else {
            assertTrue("Expected response to be valid. But got: "+response, false);
        }
    }
}