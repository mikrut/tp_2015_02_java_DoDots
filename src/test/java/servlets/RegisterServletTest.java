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

public class RegisterServletTest {
    final private static String url = "/signin";
    private static final AccountManager mgr = new MapAccountManager();
    private final RegisterServlet registerPage = new RegisterServlet(mgr);

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
    public void testRegistration() throws Exception{
        final StringWriter writer = new StringWriter();
        final String username = "username";
        final String email = "e@mail.ru";
        final String password = "pwd";

        HttpServletRequest request = getMockRequest();
        HttpServletResponse response = getMockResponse(writer);

        when(request.getParameter("name")).thenReturn(username);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);

        registerPage.doPost(request, response);
        validateResponse(writer.toString(), "OK");
        writer.getBuffer().setLength(0);

        registerPage.doPost(request, response);
        validateResponse(writer.toString(), "Error");
        writer.getBuffer().setLength(0);

        when(request.getParameter("name")).thenReturn(username+"_new");

        registerPage.doPost(request, response);
        validateResponse(writer.toString(), "OK");
        writer.getBuffer().setLength(0);
    }

    void validateResponse(String response, String expected) {
        JSONObject obj = new JSONObject(response);
        if(obj != null) {
            if(obj.has("status") && obj.has("message")) {
                assertEquals("Expected \""+expected+"\" status", expected, obj.get("status"));
            } else {
                assertTrue("Expected response to contain status string. Got only: "+response, obj.has("status"));
                assertTrue("Expected response to contain message string. Got only: "+response, obj.has("message"));
            }
        } else {
            assertTrue("Expected response to be valid. But got: "+response, false);
        }
    }
}