package User;

/**
 * Created by Михаил on 01.03.2015.
 */
import User.User;
public interface AccountManager {
    public User findUser(String username);
    public User registerUser(String username, String password);
    public void deleteUser(String username);

    public User authenticate(String username, String password);
    public void logout(String username);
}
