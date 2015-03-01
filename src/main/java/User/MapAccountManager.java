package User;

/**
 * Created by Михаил on 01.03.2015.
 */

import java.util.Map;

public class MapAccountManager implements AccountManager {
    Map<String, User> registeredList;
    Map<String, User> loggedInList;

    public User findUser(String username) {
        return registeredList.getOrDefault(username, null);
    }
    public User registerUser(String username, String password) {
        User usr = null;
        if(!registeredList.containsKey(username)) {
            usr = new User(username, password);
            registeredList.put(username, usr);
            loggedInList.put(username, usr);
        }
        return usr;
    }
    public void deleteUser(String username) {
        registeredList.remove(username);
        loggedInList.remove(username);
    }
    public User authenticate(String username, String password) {
        User usr = null;
        if((usr=findUser(username)) != null && usr.checkPassword(password)) {
            loggedInList.put(username, usr);
        } else {
            usr = null;
        }
        return usr;
    }
    public void logout(String username) {
        loggedInList.remove(username);
    }
}
