package user;

import java.util.Map;

/**
 * Created by Михаил on 01.03.2015.
 */

public interface AccountManager {
    public User findUser(String username);
    public User registerUser(String username, String password) throws Exception;
    public Map<String, User> getAllRegistered();
    public void deleteUser(String username);

    public User authenticate(String username, String password) throws Exception;
    public User getAuthenticated(Long id);
    public void logout(Long id);
}
