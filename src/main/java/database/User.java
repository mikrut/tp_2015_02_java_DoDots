package database;

/**
 * Created by Михаил
 * 01.03.2015 9:15
 * Package: ${PACKAGE_NAME}
 */
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.hibernate.Hibernate;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

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
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer score = 0;

    @SuppressWarnings("UnusedDeclaration")
    @OneToMany(fetch=FetchType.LAZY, mappedBy="user1", targetEntity = GameResults.class)
    private Set<GameResults> gamesAsFirstPlayer;
    @SuppressWarnings({"UnusedDeclaration", "MismatchedQueryAndUpdateOfCollection"})
    @OneToMany(fetch=FetchType.LAZY, mappedBy="user2", targetEntity = GameResults.class)
    private Set<GameResults> gamesAsSecondPlayer;

    @Transient
    private UserDAO parent = null;

    public User() {
    }

    public User(User usr) {
        this(usr.getUsername(), "something", usr.getEmail(), usr.getID(), usr.getStatus());
        passHash = usr.passHash;
    }

    public void setParent(UserDAO dao) {
        parent = dao;
    }

    public User(String username, String password, String email) {
        this(username, password, email, null, Rights.BASIC);
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

    void setUsername(String username) {
        this.username = username;
    }

    void setPassword(String password) {
        passHash = makePassHash(password);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getScore() {
        return score;
    }

    public void incScore(Integer sc) {
        this.score += sc;
    }

    void setUserId(Long uid) {
        if(uid != null)
            userId = uid;
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
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Long getID(){
        return userId;
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

    public Set<GameResults> getGameResults() {
        if (parent != null) {
            Session session = parent.getSession();
            session.replicate(this, ReplicationMode.LATEST_VERSION);
            Hibernate.initialize(this);

            Set<GameResults> total = new HashSet<GameResults>();
            total.addAll(gamesAsFirstPlayer);
            total.addAll(gamesAsSecondPlayer);
            session.close();
            return total;
        } else {
            return null;
        }
    }
}
