package database;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import resources.AccountManagerResource;
import resources.ResourceProvider;


import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class DAOGameResultsTest {
    private DAOGameResults gr;
    private UserDAO manager;
    private static SessionFactory factory;

    private AccountManagerResource accountManagerResource;

    @BeforeClass
    public static void init() {
        factory = UserDAOTest.getFactory(false);
    }

    @Before
    public void initialize() {
        accountManagerResource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");

        gr = new DAOGameResults(factory);
        manager = new UserDAO(factory);
    }

    @Test
    public void testSaveResults() {
        User user1, user2;
        String adminName = accountManagerResource.getAdminName();
        user1 = user2 = manager.findUser(adminName);

        int beforeCount = user1.getGameResults().size();
        gr.addResult(user1, 10, user2, 20);
        user1 = manager.findUser(adminName);
        assertEquals("Expected count of results to be greater by one after adding result",
                beforeCount+1, user1.getGameResults().size());
    }

    @Test
    public void testGameResultsUpdate() {
        User user1;
        String adminName = accountManagerResource.getAdminName();
        user1 = manager.findUser(adminName);
        int beforeCount = user1.getGameResults().size();
        gr.addResult(user1, 10, user1, 20);
        user1 = manager.findUser(adminName);
        assertEquals("Expected count of results to be greater by one after adding result",
                beforeCount+1, user1.getGameResults().size());
    }

    @Test
    public void testUserLazyInitialization() {
        User user1;
        String adminName = accountManagerResource.getAdminName();
        user1 = manager.findUser(adminName);
        gr.addResult(user1, 10, user1, 20);
        user1 = manager.findUser(adminName);
        Set<GameResults> results = user1.getGameResults();
        GameResults result = results.iterator().next();
        String name1 = result.getUser1().getUsername();
        String name2 = result.getUser2().getUsername();
        assertTrue("Expected to get our user data",
                adminName.equals(name1) || adminName.equals(name2));
    }
}