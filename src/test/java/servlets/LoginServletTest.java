package servlets;

import org.junit.Before;
import org.junit.BeforeClass;
import resources.AccountManagerResource;
import resources.ResourceProvider;
import resources.ResponseResource;
import resources.ServerPathResource;
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

import org.json.JSONObject;

public class LoginServletTest {
    private static String url;
    private static final AccountManager mgr = new MapAccountManager();
    private final LoginServlet loginPage = new LoginServlet(mgr);

    private final StringWriter writer = new StringWriter();
    private final String username = "username";
    private final String password = "pwd";
    private final String em = "email@mail";
    final private static ResponseResource respResource = (ResponseResource) ResourceProvider.getProvider().getResource("response_resource.xml");
    final private static AccountManagerResource amResource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");

    private HttpServletRequest request;
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

    @BeforeClass
    public static void initClass() {
        ServerPathResource path = (ServerPathResource) ResourceProvider.getProvider().getResource("server_path.xml");
        url = path.getLoginUrl();
    }

    @Before
    public void initialize() throws Exception {
        request = getMockRequest();
        response = getMockResponse(writer);

        when(request.getParameter("name")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);

        mgr.deleteUser(username);
        mgr.registerUser(username, password, em);
        writer.getBuffer().setLength(0);
    }

    @Test
    public void testValidInfo() throws Exception {
        loginPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertNotNull("Expected to get valid JSON response", obj);
    }

    @Test
    public void testHaveStatusInResponse() throws Exception {
        loginPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected to have status parameter", obj.has(respResource.getStatus()));
    }

    @Test
    public void testHaveMessageInResponse() throws Exception {
        loginPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected to have message parameter", obj.has(respResource.getMessage()));
    }

    @Test
    public void testSuccessLogin() throws Exception {
        loginPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertEquals("Expected login to be successful", respResource.getOk(), obj.getString(respResource.getStatus()));
    }

    @Test
    public void testErrorLogIn() throws Exception {
        when(request.getParameter("name")).thenReturn(username+"_wrong");
        loginPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertEquals("Expected login to fail", respResource.getError(), obj.getString(respResource.getStatus()));
    }

    @Test
    public void testErrorMessageLogIn() throws Exception {
        when(request.getParameter("name")).thenReturn(username+"_wrong");
        loginPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertEquals("Expected to get userNotFound message",
                amResource.getUserNotFound(),
                obj.getString(respResource.getMessage()));
    }
    @Test
    public void testErrorMessageWrongPass() throws Exception {
        when(request.getParameter("password")).thenReturn(password+"_wrong");
        loginPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertEquals("Expected to get incorrectPassword message",
                amResource.getIncorrectPassword(),
                obj.getString(respResource.getMessage()));
    }

}