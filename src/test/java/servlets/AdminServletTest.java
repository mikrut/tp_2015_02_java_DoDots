package servlets;

import org.eclipse.jetty.server.Server;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.AccountManager;
import user.MapAccountManager;
import database.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedList;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class AdminServletTest {
    final private static String url = "/admin";
    final private String adminUsername = "admin";

    private static final AccountManager mgr = new MapAccountManager();
    final private static Server server = mock(Server.class);
    private final AdminServlet adminPage = new AdminServlet(server, mgr);

    private final StringWriter writer = new StringWriter();
    private final HttpSession session = mock(HttpSession.class);
    private final HttpServletRequest request = getMockRequest();
    private HttpServletResponse response;

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

    @Before
    public void doSetUp() throws Exception{
        response = getMockResponse(writer);
        writer.getBuffer().setLength(0);

        when(session.getId()).thenReturn("dummySessionId");
        String adminPassword = "admin";
        mgr.authenticate(session.getId(), adminUsername, adminPassword);
        when(request.getSession()).thenReturn(session);
    }

    @After
    public void doClean() {
        Collection<String> users = new LinkedList<>();
        for (User user : mgr.getAllRegistered().values()) {
            if(!user.getUsername().equals(adminUsername)){
                users.add(user.getUsername());
            }
        }
        for (String name : users) {
            mgr.deleteUser(name);
        }
    }

    @Test
    public void testGetValidity() throws Exception {
        adminPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected response to be valid. But got: " + response, obj != null);
    }

    @Test
    public void testResponseConsistence() throws Exception {
        adminPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected response to contain status string. Got only: " + writer.toString(), obj.has("status"));
        assertTrue("Expected response to contain users array. Got only: " + writer.toString(), obj.has("users"));
        assertEquals("Expected \"OK\" status", "OK", obj.get("status"));
    }

    @Test
    public void testOnlyAdminOnServer() throws Exception {
        adminPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        JSONArray usersArray = ((JSONArray)obj.get("users"));
        assertEquals("Expected one user on server", 1, usersArray.length());
        assertEquals("Expected user to be admin", adminUsername, ((JSONObject) usersArray.get(0)).get("username"));
    }

    @Test
    public void testUserAndAdminOnServer() throws Exception {
        final String username = "MrPupkin";
        final String password = "you,shell,n0t_pass!!1";
        String em = "email@mail";

        final HttpSession userSession = mock(HttpSession.class);
        when(userSession.getId()).thenReturn("user_dummySessionId");

        mgr.registerUser(username, password, em);
        mgr.authenticate(userSession.getId(), username, password);

        adminPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        JSONArray usersArray = ((JSONArray)obj.get("users"));
        assertEquals("Expected two users on server", 2, usersArray.length());
        boolean found = false;
        int i = 0;
        while(!found && i<usersArray.length()) {
            if(((JSONObject) usersArray.get(i)).get("username").equals(username))
                found = true;
            i++;
        }

        assertTrue("Expected to find user with name \"" + username + "\"", found);
    }

    @Test
    public void testUserLogout() throws Exception {
        final String username = "MrPupkin";
        final String password = "you,shell,n0t_pass!!1";
        String em = "email@mail";

        final HttpSession userSession = mock(HttpSession.class);
        when(userSession.getId()).thenReturn("user_dummySessionId");

        mgr.registerUser(username, password, em);
        mgr.authenticate(userSession.getId(), username, password);
        mgr.logout(userSession.getId());
        adminPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        JSONArray usersArray = ((JSONArray)obj.get("users"));
        assertEquals("Expected two users on server after logout", 2, usersArray.length());
        boolean found = false;
        int i = 0;
        while(!found && i<usersArray.length()) {
            if(((JSONObject) usersArray.get(i)).get("username").equals(adminUsername))
                found = true;
            i++;
        }
        assertTrue("Expected to find admin", found);
    }

    @Test
    public void testUnauthorisedAccess() throws Exception {
        when(session.getId()).thenReturn("wrong_dummySessionId");
        adminPage.doGet(request, response);
        verify(response, atLeastOnce()).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testLowPrivilegeAccess() throws Exception {
        final String username = "MrPupkin";
        final String password = "you,shell,n0t_pass!!1";
        String em = "email@mail";

        final HttpSession userSession = mock(HttpSession.class);
        when(userSession.getId()).thenReturn("user_dummySessionId");
        mgr.registerUser(username, password, em);
        mgr.authenticate(userSession.getId(), username, password);
        when(request.getSession()).thenReturn(userSession);
        adminPage.doGet(request, response);
        verify(response, atLeastOnce()).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /*
    Getting failure (null pointer exception) for mock Server.
    Could not find any solution yet so I leave this test commented.

    If you find some alternative or simply refactor code in AdminServlet to use mock-server
    (http://mvnrepository.com/artifact/org.mock-server/mockserver-jetty/2.9)
    than I would appreciate your help.

    @Test
    public void testServerStop() throws Exception {
        adminPage.doPost(request, response);
        verify(server, atLeastOnce()).stop();
    }*/
}