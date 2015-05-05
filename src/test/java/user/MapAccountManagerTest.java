package user;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import resources.AccountManagerResource;
import resources.ResourceProvider;
import resources.ResponseResource;

import static junit.framework.TestCase.assertEquals;

public class MapAccountManagerTest {

    private AccountManager manager;
    private ResponseResource responseResource;
    private AccountManagerResource aMResource;

    @Before
    public void initialize(){
        manager = new MapAccountManager();
        responseResource = (ResponseResource) ResourceProvider.getProvider().getResource("response_resource.xml");
        aMResource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");
    }

    @After
    public void finish(){
        manager = null;
    }

    @Test
    public void testRegisterUser() throws Exception{
        String un = "username";
        String pw = "password";
        String em = "email@mail";
        Integer counter = manager.getUserCount();

        manager.registerUser(un, pw, em);
        assertEquals("Expected to find more users than before registration", new Integer(counter+1), manager.getUserCount());
    }

    @Test
    public void doNotRegisterSimilar() {
        String un = "username";
        String pw = "password";
        String em = "email@mail";

        manager.registerUser(un, pw, em);
        JSONObject result = manager.registerUser(un, pw, em);
        assertEquals("Expected to get error status", responseResource.getError(), result.get(responseResource.getStatus()));
        assertEquals("Expected to get error message", aMResource.getUserAlreadyExists(), result.get(responseResource.getMessage()));
    }

    @Test
    public void testDeleteUser() {
        String un = "username";
        String pw = "password";
        String em = "email@mail";
        Integer counter;

        manager.registerUser(un, pw, em);
        counter = manager.getUserCount();
        manager.deleteUser(un);
        assertEquals("Expected to get less users than before deletion", new Integer(counter - 1), manager.getUserCount());
    }

    @Test
    public void testAuthenticate() {
        String un = "username";
        String pw = "password";
        String em = "email@mail";

        manager.registerUser(un, pw, em);
        assertEquals("Expected authentication to be successful", null, manager.checkAuthable(un, pw));

        String message = manager.checkAuthable(un, "wrongpass");
        assertEquals("Expected authentication to fail because of wrong pass", aMResource.getIncorrectPassword(), message);

        message = manager.checkAuthable("wrongname", pw);
        assertEquals("Expected authentication to fail because of not existing user", aMResource.getUserNotFound(), message);
    }

    @Test
    public void testAddSession() {
        String sessionid = "session";
        String un = "username";
        String pw = "password";
        String em = "email@mail";
        Integer numBefore;

        numBefore = manager.getSessionCount();
        manager.registerUser(un, pw, em, sessionid);
        assertEquals("Expected session number after insertion to be greater than before",
                new Integer(numBefore + 1),
                manager.getSessionCount());

    }

    @Test
    public void testLogout() {
        String sessionid = "session";
        String un = "username";
        String pw = "password";
        String em = "email@mail";
        Integer numBefore;

        manager.registerUser(un, pw, em, sessionid);
        numBefore = manager.getSessionCount();
        manager.logout(sessionid);
        assertEquals("Expected session number after logout to be less than before",
                new Integer(numBefore - 1),
                manager.getSessionCount());
    }
}