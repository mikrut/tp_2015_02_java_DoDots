package database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import resources.DBResource;
import resources.ResourceProvider;

/**
 * Created by mihanik
 * 06.05.15 20:57
 * Package: database
 */
abstract class CustomSessionFactory implements SessionFactory {
    private static final SessionFactory factory = createFactory();

    private synchronized static SessionFactory createFactory() {
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
    }

    public static SessionFactory getFactory() {
        return factory;
    }
}
