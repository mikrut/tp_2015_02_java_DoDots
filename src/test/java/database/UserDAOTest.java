package database;

import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.BeforeClass;
import org.junit.Test;
import resources.DBResource;
import resources.ResourceProvider;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;

public class UserDAOTest {
    private static final Object monitor = new Object();

    private static UserDAO manager;
    private static volatile SessionFactory factory = null;

    private static UserDAO getTestDAO() {
        DBResource resource = (DBResource) ResourceProvider.getProvider().getResource("dbresource.xml");
        return getTestDAO(resource.getShowSql().equals("true"));
    }

    private static UserDAO getTestDAO(Boolean doLog) {
        return new UserDAO(getFactory(doLog));
    }

    public static SessionFactory getFactory(Boolean doLog) {
        if(factory == null) {
            synchronized (monitor) {
                if(factory == null) {
                    Configuration configuration = new Configuration();
                    configuration.addAnnotatedClass(User.class);
                    configuration.addAnnotatedClass(GameResults.class);

                    DBResource resource = (DBResource) ResourceProvider.getProvider().getResource("dbresource.xml");

                    configuration.setProperty("hibernate.dialect", resource.getDbDialect());
                    configuration.setProperty("hibernate.connection.driver_class", resource.getDbDriverClassName());
                    configuration.setProperty("hibernate.connection.url", resource.getDbURL() + resource.getDbTestName());
                    configuration.setProperty("hibernate.connection.username", resource.getDbUser());
                    configuration.setProperty("hibernate.connection.password", resource.getDbPassword());
                    configuration.setProperty("hibernate.show_sql", doLog.toString());
                    configuration.setProperty("hibernate.hbm2ddl.auto", "create");
                    configuration.setProperty("hibernate.flushMode", resource.getFlushMode());

                    StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
                    builder.applySettings(configuration.getProperties());
                    ServiceRegistry registry = builder.build();

                    factory = configuration.buildSessionFactory(registry);
                }
            }
        }
        return factory;
    }

    @BeforeClass
    public static void initialize() {
        manager = getTestDAO();
        System.out.println("Configuration complete");
    }

    @Test
    public void testHasAdmin() {
        System.out.println("Testing admin existance");
        User admin = manager.findUser("admin");
        assertNotNull("Expected to find admin in db! Found nothing!", admin);
    }

    @Test
    public void testIncScore() throws Exception {
        manager.deleteUser("username");
        User usr = new User("username", "userpassword", "email@yandex.ru");
        manager.saveUser(usr);
        usr = manager.findUser("username");
        int score1 = usr.getScore();
        int inc = 10;
        manager.incScore(usr, inc);
        usr = manager.findUser("username");
        int score2 = usr.getScore();
        assertEquals("Expected score to increase by 10", score2, inc+score1);
        manager.deleteUser(usr.getUsername());
    }

    @Test
    public void testFindUser() throws Exception {
        User user = manager.findUser("admin");
        TestCase.assertNotNull(user);
    }
}