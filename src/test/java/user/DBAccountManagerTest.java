package user;

import database.GameResults;
import database.User;
import database.UserDAO;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;
import resources.DBResource;
import resources.ResourceProvider;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;

public class DBAccountManagerTest {
    private DBAccountManager manager;

    @Before
    public void initialize() {
        System.out.println("configuring database");

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(GameResults.class);

        DBResource resource = (DBResource) ResourceProvider.getProvider().getResource("dbresource.xml");

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

        UserDAO dao = new UserDAO(factory);

        System.out.println("Configuration complete");
        manager = new DBAccountManager(dao);
    }

    @Test
    public void testSigninUser() throws Exception {
        System.out.println("Testing signin");

        manager.deleteUser("username");
        manager.registerUser("username", "userpassword", "email");
        User usr = manager.findUser("username");
        assertNotNull("Expected to get registered user. Got nothing.", usr);
        manager.deleteUser(usr.getUsername());
    }

    @Test
    public void testPersistantSignin() throws Exception {
        manager.deleteUser("username");

        manager.registerUser("username", "userpassword", "email");
        User usr = manager.findUser("username");
        assertNotNull("Expected to get registered user. Got nothing.", usr);
        manager.deleteUser(usr.getUsername());
    }

    @Test
    public void testDeleteUser() throws Exception {
        System.out.println("Testing deletion");

        manager.deleteUser("username");
        manager.registerUser("username", "userpassword", "email");
        manager.deleteUser("username");
        User usr = manager.findUser("username");
        assertNull("Expected to get nothing because of user deleted. But got user!", usr);
    }

    @Test
    public void testSaveUser() throws Exception {
        manager.deleteUser("username");
        manager.registerUser("username", "userpassword", "email@yandex.ru");
        User usr = manager.changeEmail("username", "new_email@mail.ru");
        assertEquals("Expected email to change from email@yandex.ru to new_email@mail.ru", usr.getEmail(), "new_email@mail.ru");
        manager.deleteUser(usr.getUsername());
    }
}