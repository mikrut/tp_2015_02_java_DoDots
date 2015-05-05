package user;

/**
 * Created by Михаил
 * 01.03.2015 9:15
 * Package: ${PACKAGE_NAME}
 */
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Set;

@Entity
@Table(name="users")
public class User implements Serializable {
    public enum Rights {ADMIN, BASIC}
    private SessionFactory sessionFactory = null;

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
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer score = 0;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="user1", targetEntity = GameResults.class)
    private Set<GameResults> gamesAsFirstPlayer;
    @OneToMany(fetch=FetchType.EAGER, mappedBy="user2", targetEntity = GameResults.class)
    private Set<GameResults> gamesAsSecondPlayer;

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
        this.score = 0;
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

    public Integer getScore() {
        return score;
    }

    public void incScore(Integer sc) {
        this.score += sc;
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

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Set<GameResults> getGameResults() {
        Set<GameResults> total = gamesAsFirstPlayer;
        if (total != null)
            total.addAll(gamesAsSecondPlayer);
        return total;
    }
}
