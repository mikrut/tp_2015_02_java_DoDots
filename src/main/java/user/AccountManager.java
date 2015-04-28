package user;

import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by Михаил
 * 01.03.2015 0:21
 * Package: ${PACKAGE_NAME}
 */
public interface AccountManager {
    public JSONObject registerUser(String username, String password, String email);
    public JSONObject registerUser(String username, String password, String email, String session);
    public Map<String, User> getAllRegistered();
    public void deleteUser(String username);

    public Integer getUserCount();
    public Integer getSessionCount();

    public JSONObject authenticate(String sessionId, String username, String password);
    public void addSession(String sessionId, User usr);
    public String checkAuthable(String username, String password);
    public User getAuthenticated(String sessionId);
    public void logout(String sessionId);
    public User findUser(String username);

    public void incScore(User usr, Integer score);
}
