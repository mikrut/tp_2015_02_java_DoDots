package user;

/**
 * Created by Михаил on 01.03.2015.
 */
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.security.MessageDigest;

public class User {
    public enum Rights {ADMIN, BASIC}
    private final String username;
    private final String passhash;
    private final String email;
    private final Long userid;
    private Rights status;

    public User(String username, String password, String email, Long uid) {
        this(username, password, email, uid, Rights.BASIC);
    }

    public User(String username, String password, String email, Long uid, Rights r) {
        this.username = username;
        this.passhash = makePassHash(password);
        this.email = email;
        this.userid= uid;
        this.status = r;
    }

    public void setStatus(Rights s) {
        status = s;
    }

    public Rights getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Long getID(){
        return userid;
    }

    public Boolean checkPassword(String password) {
        return passhash.equals(makePassHash(password));
    }

    private static String makePassHash(String password) {
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
