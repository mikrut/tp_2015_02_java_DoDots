package user;

/**
 * Created by Михаил on 01.03.2015.
 */
import com.sun.org.apache.xml.internal.security.utils.Base64;
import sun.plugin2.message.Message;

import java.security.MessageDigest;

public class User {
    private String username;
    private String passhash;
    private Long userid;
    //private String secretKey;

    public User(String username, String password, Long uid) {
        this.username = username;
        this.passhash = makePassHash(password);
        this.userid= uid;
    }

    /*public void setSecretKey(String key) {
        secretKey = key;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public Boolean checkSecretKey(String key) {
        return secretKey == key && key != null;
    }*/

    public String getUsername() {
        return username;
    }

    public Long getID(){
        return userid;
    }

    public Boolean checkPassword(String password) {
        return passhash.equals(makePassHash(password));
    }

    public static String makePassHash(String password) {
        String result;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            result = Base64.encode(md.digest());
        } catch (Exception e) {
            result = password;
        }
        return result;
    }
}
