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

        User admin = findUser(resource.getAdminName());
        if(admin == null) {
            registerUser(resource.getAdminName(),
                    resource.getAdminPassword(),
                    resource.getAdminEmail());
            admin = findUser(resource.getAdminName());
            admin.setStatus(User.Rights.ADMIN);
            saveUser(admin);
        }
    }

    public JSONObject registerUser(String username, String password, String email) {
        return registerUser(username, password, email, null);
    }

    public JSONObject registerUser(String username, String password, String email, String session) {
        Session registerSession = sessionFactory.openSession();
        JSONObject response = new JSONObject();

        if(username==null || password == null || email == null) {
            response.put(responseResource.getStatus(), responseResource.getError());
            response.put(responseResource.getMessage(), resource.getNullQueryAnswer());
        } else {
            User usr = (User) registerSession.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();

            if (usr == null) {
                usr = new User(username, password, email, null);

                Transaction transaction = registerSession.beginTransaction();
                registerSession.save(usr);
                transaction.commit();

                // usr = findUser(username);
                // We don't check authable after registration because we think that our class works as needed.
                // String authableMessage = checkAuthable(username, password);

                if (session != null) {
                    authenticate(session, username, password);
                }

                response.put(responseResource.getStatus(), responseResource.getOk());
                response.put(responseResource.getMessage(), resource.getRegistrationSuccess());
            } else {
                response.put(responseResource.getStatus(), responseResource.getError());
                response.put(responseResource.getMessage(), resource.getUserAlreadyExists());
            }
        }
        registerSession.close();

        return response;
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

    public void incScore(User usr, Integer score) {
        Session scoreSession = sessionFactory.openSession();
        Transaction transaction = scoreSession.beginTransaction();

        User myUser = (User) scoreSession.get(User.class, usr);
        myUser.incScore(score);
        scoreSession.save(myUser);

        transaction.commit();
        scoreSession.close();
    }

    public User findUser(String username) {
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

    public String checkAuthable(String username, String password) {
        User usr = findUser(username);

        if(usr == null)
            return resource.getUserNotFound();
        if(!usr.checkPassword(password))
            return resource.getIncorrectPassword();

        return null;
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
