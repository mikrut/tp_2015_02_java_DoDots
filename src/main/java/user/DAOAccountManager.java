package user;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONObject;
import resources.AccountManagerResource;
import resources.ResourceProvider;
import resources.ResponseResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by mihanik
 * 19.04.15 14:53
 * Package: user
 *
 * Откуда вдруг такая лень?
 * Едва меня сегодня добудились...
 * Шумит весенний дождь.
 */

public class DAOAccountManager implements AccountManager {
    private final Map<String, User> loggedInList = new HashMap<>();
    private AccountManagerResource resource = null;
    private ResponseResource responseResource = null;
    private SessionFactory sessionFactory;

    public DAOAccountManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

        resource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");
        responseResource = (ResponseResource) ResourceProvider.getProvider().getResource("response_resource.xml");
        try {
            User admin = registerUser(resource.getAdminName(),
                    resource.getAdminPassword(),
                    resource.getAdminEmail());
            admin.setStatus(User.Rights.ADMIN);
            saveUser(admin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User registerUser(String username, String password, String email) throws Exception {
        Session registerSession = sessionFactory.openSession();

        if(username==null || password == null || email == null)
            throw new Exception(resource.getNullQueryAnswer());

        User usr = (User) registerSession.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();

        if(usr == null) {
            usr = new User(username, password, email, null);

            Transaction transaction = registerSession.beginTransaction();
            registerSession.save(usr);
            transaction.commit();

            usr = findUser(username);
            try {
                checkAuthable(username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registerSession.close();
            throw new Exception(resource.getUserAlreadyExists());
        }
        registerSession.close();

        return usr;
    }

    public Map<String, User> getAllRegistered() {
        Session searchSession = sessionFactory.openSession();
        Criteria criteria = searchSession.createCriteria(User.class);
        List<User> users = criteria.list();
        Map<String, User> usersMap = new HashMap<>();
        for(User user:users) {
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
        for(Map.Entry<String,User> record : loggedInList.entrySet())
            if(record.getValue().getUsername().equals(username))
                logout(record.getKey());

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

    public Integer getSessionCount() {
        return loggedInList.size();
    }

    protected User findUser(String username) {
        Session querySession = sessionFactory.openSession();
        return (User) querySession.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
    }

    public JSONObject authenticate(String sessionId, String username, String password) {
        User usr = findUser(username);
        JSONObject response = new JSONObject();

        if(usr != null){
            if(!usr.checkPassword(password)) {
                response.put(responseResource.getStatus(), responseResource.getError());
                response.put(responseResource.getMessage(), resource.getIncorrectPassword());
            } else {
                response.put(responseResource.getStatus(), responseResource.getOk());
                response.put(responseResource.getMessage(), resource.getAuthSuccess());
                addSession(sessionId, usr);
            }
        } else {
            response.put(responseResource.getStatus(), responseResource.getError());
            response.put(responseResource.getMessage(), resource.getUserNotFound());
        }
        return response;
    }

    public User checkAuthable(String username, String password) throws Exception {
        User usr = findUser(username);

        if(usr != null){
            if(!usr.checkPassword(password)) {
                throw new Exception(resource.getIncorrectPassword());
            }
        } else {
            throw new Exception(resource.getUserNotFound());
        }
        return usr;
    }

    public void addSession(String sessionId, User usr) {
        loggedInList.put(sessionId, usr);
    }

    public User getAuthenticated(String sessionId) {
        return loggedInList.get(sessionId);
    }

    public void logout(String sessionId) {
        loggedInList.remove(sessionId);
    }
}
