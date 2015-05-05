package user;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import resources.DBResource;
import resources.ResourceProvider;

/**
 * Created by mihanik
 * 23.04.15 10:56
 * Package: user
 */
public class DAManager {
    private final DAOAccountManager manager;
    private final DAOGameResults gameResults;

    private static final SessionFactory factory = null;
    static private final DAManager singleton = new DAManager();

    public DAManager() {
        manager = new DAOAccountManager(getFactory());
        gameResults = new DAOGameResults(getFactory());

        User admin = manager.findUser("admin");
        if (admin.getGameResults().size() == 0)
            gameResults.addResult(admin, 10, admin, 20);
    }

    public AccountManager getAccountManager() {
        return manager;
    }

    public DAOGameResults getGameResults() {
        return gameResults;
    }

    private static SessionFactory getFactory() {
        //noinspection ConstantConditions
        if (factory == null) {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(GameResults.class);

            DBResource resource = (DBResource) ResourceProvider.getProvider().getResource("dbresource.xml");

            configuration.setProperty("hibernate.dialect", resource.getDbDialect());
            configuration.setProperty("hibernate.connection.driver_class", resource.getDbDriverClassName());
            configuration.setProperty("hibernate.connection.url", resource.getDbURL() + resource.getDbName());
            configuration.setProperty("hibernate.connection.username", resource.getDbUser());
            configuration.setProperty("hibernate.connection.password", resource.getDbPassword());
            configuration.setProperty("hibernate.show_sql", resource.getShowSql());
            configuration.setProperty("hibernate.hbm2ddl.auto", resource.getHbm2DdlAuto());
            configuration.setProperty("hibernate.flushMode", resource.getFlushMode());

            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
            builder.applySettings(configuration.getProperties());
            ServiceRegistry registry = builder.build();

            return configuration.buildSessionFactory(registry);
        } else {
            return factory;
        }
    }

    public static DAManager getSingleton() {
        return singleton;
    }
}
