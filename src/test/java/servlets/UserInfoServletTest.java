package servlets;

import junit.framework.Assert;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import resources.AccountManagerResource;
import resources.ResourceProvider;
import resources.ResponseResource;
import resources.ServerPathResource;
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
    private static String url;
    private static final AccountManager mgr = new MapAccountManager();
    private final UserInfoServlet infoPage = new UserInfoServlet(mgr);

    final StringWriter writer = new StringWriter();
    final String username = "username";
    final String password = "pwd";
    final String em = "email@mail";
    final HttpSession session = mock(HttpSession.class);

    private HttpServletRequest request;
    private HttpServletResponse response;

    private static ResponseResource responseResource;
    private static AccountManagerResource amResource;

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

    @BeforeClass
    public static void initClass() {
        ServerPathResource srvPath = (ServerPathResource) ResourceProvider.getProvider().getResource("server_path.xml");
        url = srvPath.getUserInfoUrl();

        responseResource = (ResponseResource) ResourceProvider.getProvider().getResource("response_resource.xml");
        amResource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");
    }

    @Before
    public void initialize() throws Exception{
        request = getMockRequest();
        response = getMockResponse(writer);

        when(session.getId()).thenReturn("dummysessionid");
        when(request.getParameter("name")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getSession()).thenReturn(session);
        mgr.deleteUser(username);
        mgr.registerUser(username, password, em);
        mgr.authenticate(session.getId(), username, password);

        writer.getBuffer().setLength(0);
    }

    @Test
    public void testGetResponse() throws Exception {
        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertNotNull("Expected user to get valid JSON response", obj);
    }

    @Test
    public void testResponseContainsUsername() throws Exception {
        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected response to contain username", obj.has(amResource.getUsernameAPIName()));
    }

    @Test
    public void testResponseContainsEmail() throws Exception {
        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected response to contain email", obj.has(amResource.getEmailAPIName()));
    }

    @Test
    public void testResponseContainsScore() throws Exception {
        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected response to contain score", obj.has(amResource.getScoreAPIName()));
    }

    @Test
    public void testResponseContainsResults() throws Exception {
        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected response to contain results", obj.has(amResource.getResultsAPIName()));
    }

    @Test
    public void testDontTellPassword() throws Exception {
        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertFalse("Expected not to tell password", obj.has("password"));
    }

    @Test
    public void testLogin() throws Exception {
        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected user to be logged in", obj.getBoolean(amResource.getLoggedInAPIName()));
    }

    @Test
    public void testGetRightUsername() throws Exception {
        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertEquals("Expected test username and response username to be equal",
                username,
                obj.get(amResource.getUsernameAPIName()));
    }

    @Test
    public void testGetRightEmail() throws Exception {
        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertEquals("Expected test email and response email to be equal",
                em,
                obj.get(amResource.getEmailAPIName()));
    }

    @Test
    public void testGuestNotLoggedIn() throws Exception {
        when(session.getId()).thenReturn("dummysessionid_wrong");

        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertFalse("Expected guest to be not logged in", obj.getBoolean(amResource.getLoggedInAPIName()));
    }

    @Test
    public void testGuestToHaveGuestName() throws Exception {
        when(session.getId()).thenReturn("dummysessionid_wrong");

        infoPage.doGet(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertEquals("Expected guest to have guest name", amResource.getGuestName(), obj.getString(amResource.getUsernameAPIName()));
    }
}