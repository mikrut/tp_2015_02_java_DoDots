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

        registerUser(resource.getAdminName(),
                     resource.getAdminPassword(),
                     resource.getAdminEmail());
        User admin = findUser(resource.getAdminName());
        admin.setStatus(User.Rights.ADMIN);
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

    public User findUser(String username) {
        User usr = registeredList.getOrDefault(username, null);
        return usr;
    }

    public JSONObject registerUser(String username, String password, String email) {
        return registerUser(username, password, email, null);
    }

    public JSONObject registerUser(String username, String password, String email, String session) {
        JSONObject response = new JSONObject();

        if(username==null || password == null || email == null) {
            response.put(responseResource.getStatus(), responseResource.getError());
            response.put(responseResource.getMessage(), resource.getNullQueryAnswer());
        } else {
            User usr;

            if (!registeredList.containsKey(username)) {
                usr = new User(username, password, email, userIdGenerator.getAndIncrement());

                registeredList.put(username, usr);

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

        return response;
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

    public String checkAuthable(String username, String password) {
        User usr = findUser(username);

        if(usr == null)
            return resource.getUserNotFound();
        if(!usr.checkPassword(password))
            return resource.getIncorrectPassword();

        return null;
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