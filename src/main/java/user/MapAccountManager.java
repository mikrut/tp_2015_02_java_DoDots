package user;

/**
 * Created by Михаил
 * 01.03.2015 9:15
 * Package: ${PACKAGE_NAME}
 */
import org.json.simple.JSONObject;
import resources.AccountManagerResource;
import resources.ResourceProvider;
import resources.ResponseResource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class MapAccountManager implements AccountManager {
    private final Map<String, User> registeredList = new HashMap<>();
    private final Map<String, User> loggedInList = new HashMap<>();
    private final AtomicLong userIdGenerator = new AtomicLong();
    private AccountManagerResource resource = null;
    private ResponseResource responseResource = null;

    private static final AccountManager singleton_manager = new MapAccountManager();

    public MapAccountManager() {
        resource = (AccountManagerResource) ResourceProvider.getProvider().getResource("account.xml");
        responseResource = (ResponseResource) ResourceProvider.getProvider().getResource("response_resource.xml");
        try {
            User admin = registerUser(resource.getAdminName(),
                                      resource.getAdminPassword(),
                                      resource.getAdminEmail());
            admin.setStatus(User.Rights.ADMIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AccountManager getManager() {
        return singleton_manager;
    }

    public Map<String, User> getAllRegistered(){
        return registeredList;
    }

    public Integer getUserCount() {
        return registeredList.size();
    }

    public Integer getSessionCount() {
        return loggedInList.size();
    }

    User findUser(String username) {
        return new User(registeredList.getOrDefault(username, null));
    }

    public User registerUser(String username, String password, String email) throws Exception {
        User usr;

        if(username==null || password == null || email == null)
            throw new Exception(resource.getNullQueryAnswer());

        if(!registeredList.containsKey(username)) {
            usr = new User(username, password, email, userIdGenerator.getAndIncrement());

            registeredList.put(username, usr);
            try {
                checkAuthable(username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new Exception(resource.getUserAlreadyExists());
        }
        return usr;
    }

    public void deleteUser(String username) {
        if (registeredList.containsKey(username)) {
            for(Map.Entry<String,User> record : loggedInList.entrySet()) {
                if(record.getValue().getUsername().equals(username))
                    logout(record.getKey());
            }
            registeredList.remove(username);
        }
    }

    @SuppressWarnings("unchecked")
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

    public void addSession(String sessionId, User usr){
        loggedInList.put(sessionId, usr);
    }

    public User getAuthenticated(String sessionId) {
        return loggedInList.get(sessionId);
    }

    public void logout(String sessionId) {
        loggedInList.remove(sessionId);
    }
}