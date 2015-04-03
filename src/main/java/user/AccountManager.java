package user;

import java.util.Map;

/**
 * Created by Михаил
 * 01.03.2015 0:21
 * Package: ${PACKAGE_NAME}
 */
public interface AccountManager {
    public User findUser(String username);
    public User registerUser(String username, String password, String email) throws Exception;
    public Map<String, User> getAllRegistered();
    public void deleteUser(String username);

    public Integer getUserCount();
    public Integer getSessionCount();

    public void authenticate(String sessionId, String username, String password) throws Exception;
    public User checkAuthable(String username, String password) throws Exception;
    public void addSession(String sessionId, User usr);
    public User getAuthenticated(String sessionId);
    public void logout(String sessionId);
}
