package database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;
import resources.AccountManagerResource;
import resources.DBResource;
import resources.ResourceProvider;


import static junit.framework.TestCase.assertEquals;

public class DAOGameResultsTest {
    private DAOGameResults gr;
    private UserDAO manager;

    private AccountManagerResource accountManagerResource;

    @Before
    public void initialize() {
        accountManagerResource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");
        SessionFactory factory = UserDAOTest.getFactory(false);

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
}