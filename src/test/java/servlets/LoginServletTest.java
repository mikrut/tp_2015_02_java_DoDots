package servlets;

import user.*;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class LoginServletTest {
    final private static String url = "/login";
    private static final AccountManager mgr = new MapAccountManager();
    private final LoginServlet loginPage = new LoginServlet(mgr);

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

        HttpServletRequest request = getMockRequest();
        HttpServletResponse response = getMockResponse(writer);

        mgr.registerUser(username, password, em);

        when(request.getParameter("name")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);

        loginPage.doPost(request, response);
        validateResponse(writer.toString(), "OK");
        writer.getBuffer().setLength(0);

        when(request.getParameter("name")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("admin");

        loginPage.doPost(request, response);
        validateResponse(writer.toString(), "OK");
        writer.getBuffer().setLength(0);

        when(request.getParameter("name")).thenReturn(username+"_wrong");

        loginPage.doPost(request, response);
        validateResponse(writer.toString(), "Error");
        writer.getBuffer().setLength(0);

        when(request.getParameter("name")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password+"_wrong");

        loginPage.doPost(request, response);
        validateResponse(writer.toString(), "Error");
    }

    void validateResponse(String response, String expected) {
        JSONObject obj = (JSONObject) JSONValue.parse(response);
        if(obj != null) {
            if(obj.containsKey("status") && obj.containsKey("message")) {
                assertEquals("Expected \""+expected+"\" status", expected, obj.get("status"));
            } else {
                assertTrue("Expected response to contain status string. Got only: "+response, obj.containsKey("status"));
                assertTrue("Expected response to contain message string. Got only: "+response, obj.containsKey("message"));
            }
        } else {
            assertTrue("Expected response to be valid. But got: "+response, obj != null);
        }
    }
}