package user;

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
    private DAOAccountManager manager;

    private AccountManagerResource accountManagerResource;

    @Before
    public void initialize() {
        System.out.println("configuring database");

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(GameResults.class);

        DBResource resource = (DBResource) ResourceProvider.getProvider().getResource("dbresource.xml");
        accountManagerResource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");

        configuration.setProperty("hibernate.dialect",                 resource.getDbDialect());
        configuration.setProperty("hibernate.connection.driver_class", resource.getDbDriverClassName());
        configuration.setProperty("hibernate.connection.url",          resource.getDbURL()+ resource.getDbTestName());
        configuration.setProperty("hibernate.connection.username",     resource.getDbUser());
        configuration.setProperty("hibernate.connection.password",     resource.getDbPassword());
        configuration.setProperty("hibernate.show_sql",                resource.getShowSql());
        configuration.setProperty("hibernate.hbm2ddl.auto",            "create");
        configuration.setProperty("hibernate.flushMode",               resource.getFlushMode());

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry registry = builder.build();

        SessionFactory factory = configuration.buildSessionFactory(registry);

        gr = new DAOGameResults(factory);
        manager = new DAOAccountManager(factory);

        System.out.println("Configuration complete");
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
        String sessionid = "sessionid";
        String adminName = accountManagerResource.getAdminName();
        manager.authenticate(sessionid, adminName, accountManagerResource.getAdminPassword());
        user1 = manager.getAuthenticated(sessionid);
        int beforeCount = user1.getGameResults().size();
        gr.addResult(user1, 10, user1, 20);
        user1 = manager.getAuthenticated(sessionid);
        assertEquals("Expected count of results to be greater by one after adding result",
                beforeCount+1, user1.getGameResults().size());
    }
}