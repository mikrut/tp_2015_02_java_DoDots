package user;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class MapAccountManagerTest {

    private AccountManager manager;

    @Before
    public void initialize(){
        manager = new MapAccountManager();
    }

    @After
    public void finish(){
        manager = null;
    }

    @Test
    public void testRegisterUser() throws Exception{
        String un = "username";
        String pw = "password";
        Integer counter = manager.getUserCount();
        try {
            manager.registerUser(un, pw);
            assertEquals("Expected to find more users than before registration", new Integer(counter+1), manager.getUserCount());
        } catch(Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    @Test
    public void doNotRegisterSimilar() {
        String un = "username";
        String pw = "password";
        Integer counter = manager.getUserCount();
        try {
            manager.registerUser(un, pw);
            try {
                manager.registerUser(un, pw);
                assertTrue("No exception? That's a pity... You should throw one", false);
            } catch (Exception e) {
                assertEquals("Expected user already exists exception", "This username already exists", e.getMessage());
            }
        } catch(Exception e) {
            assertTrue("Unexpected exception: "+e.getMessage(), false);
        }
    }

    @Test
    public void testDeleteUser() {
        String un = "username";
        String pw = "password";
        Integer counter;
        try {
            manager.registerUser(un, pw);
            counter = manager.getUserCount();
            manager.deleteUser(un);
            assertEquals("Expected to get less users than before deletion", new Integer(counter - 1), manager.getUserCount());
        } catch(Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    @Test
    public void testAuthenticate() {
        String un = "username";
        String pw = "password";
        try {
            manager.registerUser(un, pw);
            assertTrue("Expected authentication to be successful", manager.checkAuthable(un, pw)!=null);
            try {
                manager.checkAuthable(un, "wrongpass");
                assertTrue("Expected authentication to fail", false);
            } catch(Exception e) {
                assertEquals("Expected authentication to fail because of wrong pass",
                        "Incorrect password", e.getMessage());
            }
            try {
                manager.checkAuthable("wrongname", pw);
                assertTrue("Expected authentication to fail", false);
            } catch(Exception e) {
                assertEquals("Expected authentication to fail because of not existing user",
                        "User not found", e.getMessage());
            }
        } catch(Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    @Test
    public void testAddSession() {
        String sessionid = "session";
        String un = "username";
        String pw = "password";
        Integer numBefore;
        User usr;
        try {
            manager.registerUser(un, pw);
            usr = manager.checkAuthable(un, pw);
            numBefore = manager.getSessionCount();
            manager.addSession(sessionid, usr);
            assertEquals("Expected session number after insertion to be greater than before",
                    new Integer(numBefore + 1),
                    manager.getSessionCount());
        } catch(Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    @Test
    public void testLogout() {
        String sessionid = "session";
        String un = "username";
        String pw = "password";
        Integer numBefore;
        User usr;
        try {
            manager.registerUser(un, pw);
            manager.authenticate(sessionid, un, pw);
            numBefore = manager.getSessionCount();
            manager.logout(sessionid);
            assertEquals("Expected session number after logout to be less than before",
                    new Integer(numBefore - 1),
                    manager.getSessionCount());
        } catch(Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }
}