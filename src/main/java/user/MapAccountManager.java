package user;

/**
 * Created by Михаил on 01.03.2015.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class MapAccountManager implements AccountManager {
    private Map<String, User> registeredList = new HashMap<>();
    private Map<Long, User> loggedInList = new HashMap<>();
    private AtomicLong userIdGenerator = new AtomicLong();

    private static AccountManager singleton_manager = new MapAccountManager();

    private MapAccountManager() {
        User admin = registerUser("admin", "admin");
        admin.setStatus(User.Rights.ADMIN);
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
    public User registerUser(String username, String password) {
        User usr = null;
        if(!registeredList.containsKey(username)) {
            usr = new User(username, password, userIdGenerator.getAndIncrement());
            registeredList.put(username, usr);
            authenticate(username, password);
        }
        return usr;
    }
    public void deleteUser(String username) {
        if (registeredList.containsKey(username)) {
            loggedInList.remove(registeredList.get(username).getID());
            registeredList.remove(username);
        }
    }
    public User authenticate(String username, String password) {
        User usr = null;
        Boolean b;
        if((usr=findUser(username)) != null && (b = usr.checkPassword(password))) {
            loggedInList.put(usr.getID(), usr);
        } else {
            usr = null;
        }
        return usr;
    }

    public User getAuthenticated(Long id) {
        return loggedInList.get(id);
    }

    public void logout(Long id) {
        loggedInList.remove(id);
    }
}
