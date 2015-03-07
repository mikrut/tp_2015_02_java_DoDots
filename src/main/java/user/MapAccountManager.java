package user;

/**
 * Created by Михаил on 01.03.2015.
 */

import com.sun.xml.internal.ws.util.QNameMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

public class MapAccountManager implements AccountManager {
    private Map<String, User> registeredList = new HashMap<>();
    private Map<String, User> loggedInList = new HashMap<>();
    private AtomicLong userIdGenerator = new AtomicLong();

    private static AccountManager singleton_manager = new MapAccountManager();

    private MapAccountManager() {
        try {
            User admin = registerUser("admin", "admin");
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

    public User findUser(String username) {
        return registeredList.getOrDefault(username, null);
    }

    public User registerUser(String username, String password) throws Exception {
        User usr = null;

        if(!registeredList.containsKey(username)) {
            usr = new User(username, password, userIdGenerator.getAndIncrement());

            registeredList.put(username, usr);
            try {
                authenticate(username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new Exception("This username already exists");
        }
        return usr;
    }

    public void deleteUser(String username) {
        Vector<String> sessionIds = new Vector<String>();

        if (registeredList.containsKey(username)) {
            for(Map.Entry<String,User> record : loggedInList.entrySet()) {
                if(record.getValue().getUsername().equals(username))
                    logout(record.getKey());
            }
            registeredList.remove(username);
        }
    }
    public User authenticate(String username, String password) throws Exception {
        User usr = null;

        if((usr=findUser(username)) != null){
            if(!usr.checkPassword(password)) {
                throw new Exception("Incorrect password");
            }
        } else {
            throw new Exception("User not found");
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
