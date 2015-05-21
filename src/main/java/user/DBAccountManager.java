package user;

import database.DAManager;
import database.User;
import database.UserDAO;
import org.json.JSONObject;
import resources.AccountManagerResource;
import resources.ResourceProvider;
import resources.ResponseResource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mihanik
 * 06.05.15 20:30
 * Package: user
 */
public class DBAccountManager implements AccountManager {
    private final Map<String, User> loggedInList = new HashMap<>();
    private AccountManagerResource resource = null;
    private ResponseResource responseResource = null;
    private final UserDAO dao;

    public DBAccountManager() {
        this(DAManager.getSingleton().getUserDAO());
    }

    public DBAccountManager(UserDAO db) {
        dao = db;
        resource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");
        responseResource = (ResponseResource) ResourceProvider.getProvider().getResource("response_resource.xml");

        User admin = dao.findUser(resource.getAdminName());
        if(admin == null) {
            registerUser(resource.getAdminName(),
                    resource.getAdminPassword(),
                    resource.getAdminEmail());
            admin = dao.findUser(resource.getAdminName());
            admin.setStatus(User.Rights.ADMIN);
            dao.saveUser(admin);
        }
    }

    public JSONObject registerUser(String username, String password, String email) {
        return registerUser(username, password, email, null);
    }

    @SuppressWarnings("unchecked")
    public JSONObject registerUser(String username, String password, String email, String session) {
        JSONObject response = new JSONObject();

        if(username==null || password == null || email == null) {
            response.put(responseResource.getStatus(), responseResource.getError());
            response.put(responseResource.getMessage(), resource.getNullQueryAnswer());
        } else {
            User usr = dao.findUser(username);

            if (usr == null) {
                usr = new User(username, password, email, null);

                dao.saveUser(usr);
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

        return response;
    }

    public Map<String, User> getAllRegistered() {
        return dao.getAllRegistered();
    }

    @Override
    public void deleteUser(String username) {
        dao.deleteUser(username);
    }

    @Override
    public Integer getUserCount() {
        return dao.getUserCount();
    }

    public Integer getSessionCount() {
        return loggedInList.size();
    }

    @SuppressWarnings("SameParameterValue")
    public User findUser(String name) {
        return dao.findUser(name);
    }

    @SuppressWarnings("SameParameterValue")
    public User changeEmail(String name, String email) {
        User usr = dao.findUser(name);
        if (usr != null) {
            usr.setEmail(email);
            dao.saveUser(usr);
            usr = dao.findUser(name);
        }
        return usr;
    }

    @SuppressWarnings("unchecked")
    public JSONObject authenticate(String sessionId, String username, String password) {
        User usr = dao.findUser(username);
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
        User usr = dao.findUser(username);

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
        if (loggedInList.containsKey(sessionId)) {
            User usr = loggedInList.get(sessionId);
            loggedInList.put(sessionId, dao.findUser(usr.getUsername()));
        }
        return loggedInList.get(sessionId);
    }

    public void logout(String sessionId) {
        loggedInList.remove(sessionId);
    }
}
