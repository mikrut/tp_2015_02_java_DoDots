package user;

import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;

public class DAOAccountManagerTest {
    static DAOAccountManager manager;

    @BeforeClass
    public static void initialize() {
        System.out.println("configuring database");

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);

        configuration.setProperty("hibernate.dialect",                 "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url",          "jdbc:mysql://localhost:3306/tp_dodots_test");
        configuration.setProperty("hibernate.connection.username",     "root");
        configuration.setProperty("hibernate.connection.password",     "0000");
        configuration.setProperty("hibernate.show_sql",                "true");
        configuration.setProperty("hibernate.hbm2ddl.auto",            "create");
        configuration.setProperty("hibernate.flushMode",               "COMMIT");

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry registry = builder.build();

        SessionFactory factory = configuration.buildSessionFactory(registry);

        manager = new DAOAccountManager(factory);

        System.out.println("Configuration complete");
    }

    @Test
    public void testHasAdmin() {
        System.out.println("Testing admin existance");
        User admin = manager.findUser("admin");
        assertNotNull("Expected to find admin in db! Found nothing!", admin);
    }


    @Test
    public void testSigninUser() throws Exception {
        System.out.println("Testing signin");

        manager.deleteUser("username");
        User usr = manager.registerUser("username", "userpassword", "email");
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
        User usr = manager.registerUser("username", "userpassword", "email");
        manager.deleteUser(usr.getUsername());
        usr = manager.findUser(usr.getUsername());
        assertNull("Expected to get nothing because of user deleted. But got user!", usr);
    }

    @Test
    public void testSaveUser() throws Exception {
        manager.deleteUser("username");
        User usr = manager.registerUser("username", "userpassword", "email@yandex.ru");
        usr.setEmail("new_email@mail.ru");
        manager.saveUser(usr);
        usr = manager.findUser(usr.getUsername());
        assertEquals("Expected email to change from email@yandex.ru to new_email@mail.ru", usr.getEmail(), "new_email@mail.ru");
        manager.deleteUser(usr.getUsername());
    }
}