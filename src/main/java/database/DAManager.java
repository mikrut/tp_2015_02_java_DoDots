package database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import resources.DBResource;
import resources.ResourceProvider;
import user.AccountManager;

/**
 * Created by mihanik
 * 23.04.15 10:56
 * Package: user
 */
public class DAManager {
    private final UserDAO manager;
    private final DAOGameResults gameResults;

    static private final DAManager singleton = new DAManager();

    private DAManager() {
        manager = new UserDAO(CustomSessionFactory.getFactory());
        gameResults = new DAOGameResults(CustomSessionFactory.getFactory());

        User admin = manager.findUser("admin");
        if (admin.getGameResults().size() == 0)
            gameResults.addResult(admin, 10, admin, 20);
    }

    public UserDAO getUserDAO() {
        return manager;
    }

    public DAOGameResults getGameResultsDAO() {
        return gameResults;
    }

    public static DAManager getSingleton() {
        return singleton;
    }
}
