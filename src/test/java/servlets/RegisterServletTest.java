package servlets;

import junit.framework.Assert;
import org.eclipse.jetty.server.Server;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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

public class RegisterServletTest {
    private static String url;
    private AccountManager mgr;
    private RegisterServlet registerPage;

    final StringWriter writer = new StringWriter();
    final String username = "username";
    final String email = "e@mail.ru";
    final String password = "pwd";

    HttpServletRequest request;
    HttpServletResponse response;

    private static ResponseResource responseResource;

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
        responseResource = (ResponseResource) ResourceProvider.getProvider().getResource("response_resource.xml");
        ServerPathResource srvResource = (ServerPathResource) ResourceProvider.getProvider().getResource("server_path.xml");
        url = srvResource.getSigninUrl();
    }

    @Before
    public void initialize() throws Exception {
        request = getMockRequest();
        response = getMockResponse(writer);

        mgr = new MapAccountManager();
        registerPage = new RegisterServlet(mgr);

        when(request.getParameter("name")).thenReturn(username);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);

        writer.getBuffer().setLength(0);
    }

    @Test
    public void testGetResponse() throws Exception {
        registerPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertNotNull("Expected to get valid JSON response", obj);
    }

    @Test
    public void testHaveStatus() throws Exception {
        registerPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected to have status in response", obj.has(responseResource.getStatus()));
    }

    @Test
    public void testHaveMessage() throws Exception {
        registerPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertTrue("Expected to have message in response", obj.has(responseResource.getMessage()));
    }

    @Test
    public void testSuccessRegistration() throws Exception {
        mgr.deleteUser(username);
        registerPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertEquals(responseResource.getOk(), obj.get(responseResource.getStatus()));
    }

    @Test
    public void testErrorDoubleSignin() throws Exception {
        mgr.deleteUser(username);
        registerPage.doPost(request, response);
        writer.getBuffer().setLength(0);
        registerPage.doPost(request, response);
        JSONObject obj = new JSONObject(writer.toString());
        assertEquals(responseResource.getError(), obj.get(responseResource.getStatus()));
    }
}