package database;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import resources.AccountManagerResource;
import resources.ResourceProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mihanik
 * 19.04.15 14:53
 * Package: user
 */

public class UserDAO {
    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        AccountManagerResource resource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");

        User admin = findUser(resource.getAdminName());

        if(admin == null) {
            admin = new User(resource.getAdminName(),
                    resource.getAdminPassword(),
                    resource.getAdminEmail());
            admin.setStatus(User.Rights.ADMIN);
            saveUser(admin);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, User> getAllRegistered() {
        Session searchSession = sessionFactory.openSession();
        Criteria criteria = searchSession.createCriteria(User.class);
        List<User> users = criteria.list();
        Map<String, User> usersMap = new HashMap<>();
        for(User user:users) {
            user.setParent(this);
            usersMap.put(user.getUsername(), user);
        }
        searchSession.close();
        return usersMap;
    }

    public void saveUser(User user) {
        deleteUser(user.getUsername());

        Session saveSession = sessionFactory.openSession();
        Transaction transaction = saveSession.beginTransaction();
        saveSession.save(user);
        transaction.commit();
        saveSession.close();
    }

    public void deleteUser(String username) {
        Session deleteSession = sessionFactory.openSession();
        Transaction transaction = deleteSession.beginTransaction();

        User usr = (User) deleteSession.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
        if(usr != null)
            deleteSession.delete(usr);

        transaction.commit();
        deleteSession.close();
    }

    public Integer getUserCount() {
        Session querySession = sessionFactory.openSession();
        Integer count = ((Long) querySession.createCriteria(User.class).setProjection(Projections.rowCount()).uniqueResult()).intValue();
        querySession.close();
        return count;
    }

    public void incScore(User usr, Integer score) {
        Session scoreSession = sessionFactory.openSession();
        Transaction transaction = scoreSession.beginTransaction();

        User myUser = (User) scoreSession.get(User.class, usr.getID());
        myUser.incScore(score);
        scoreSession.save(myUser);

        transaction.commit();
        scoreSession.close();
    }

    public User findUser(String username) {
        Session querySession = sessionFactory.openSession();
        Transaction transaction = querySession.beginTransaction();
        User usr = (User) querySession.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
        if (usr != null)
            usr.setParent(this);
        transaction.commit();
        querySession.close();
        return usr;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
