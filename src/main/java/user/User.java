package user;

/**
 * Created by Михаил
 * 01.03.2015 9:15
 * Package: ${PACKAGE_NAME}
 */
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.persistence.*;
import java.io.Serializable;
import java.security.MessageDigest;

@Entity
@Table(name="users")
public class User implements Serializable {
    public enum Rights {ADMIN, BASIC}

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(unique=true)
    private String username;
    @Column(name="password")
    private String passHash;
    @Column
    private String email;
    @Column
    private Rights status;

    public User() {
    }

    public User(User usr) {
        this(usr.getUsername(), "something", usr.getEmail(), usr.getID(), usr.getStatus());
        passHash = new String(usr.passHash);
    }

    public User(String username, String password, String email, Long uid) {
        this(username, password, email, uid, Rights.BASIC);
    }

    private User(String username, String password, String email, Long uid, Rights r) {
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
        this.setUserId(uid);
        this.setStatus(r);
    }

    public void setUsername(String username) {
        this.username = new String(username);
    }

    public void setPassword(String password) {
        passHash = makePassHash(password);
    }

    public void setEmail(String email) {
        this.email = new String(email);
    }

    public void setUserId(Long uid) {
        if(uid != null)
            userId = new Long(uid);
        else
            userId = null;
    }

    public void setStatus(Rights s) {
        status = s;
    }

    public Rights getStatus() {
        return status;
    }

    public String getUsername() {
        return new String(username);
    }

    public String getEmail() {
        return new String(email);
    }

    public Long getID(){
        return new Long(userId);
    }

    public Boolean checkPassword(String password) {
        return passHash.equals(makePassHash(password));
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
