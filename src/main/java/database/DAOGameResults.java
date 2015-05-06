package database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Created by mihanik
 * 05.05.15 19:26
 * Package: user
 */
public class DAOGameResults {
    private final SessionFactory sessionFactory;
    public DAOGameResults(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addResult(User user1, long score1, User user2, long score2) {
        Session saveSession = sessionFactory.openSession();
        Transaction transaction = saveSession.beginTransaction();

        GameResults results = new GameResults(user1, score1, user2, score2);
        saveSession.save(results);

        transaction.commit();
        saveSession.close();
    }
}
