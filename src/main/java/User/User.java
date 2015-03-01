package User;

/**
 * Created by Михаил on 01.03.2015.
 */
import java.security.MessageDigest;

public class User {
    private String username;
    private String passhash;

    public User(String username, String password) {
        this.username = username;
        this.passhash = makePassHash(password);
    }

    public Boolean checkPassword(String password) {
        return passhash == makePassHash(password);
    }

    public static String makePassHash(String password) {
        String result;
        try {
            result = MessageDigest.getInstance("MD5").digest(password.getBytes("UTF-8")).toString();
        } catch (Exception e) {
            result = password;
        }
        return result;
    }
}
