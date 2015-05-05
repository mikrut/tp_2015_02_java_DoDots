package user;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mihanik
 * 05.05.15 18:50
 * Package: user
 */


@Entity
@Table(name="results")
public class GameResults implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long resultId;

    @ManyToOne(fetch=FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name="USER1_ID", nullable = false)
    private User user1;

    @ManyToOne(fetch=FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name="USER2_ID", nullable = false)
    private User user2;

    @Column(nullable = false)
    private long user1Score;

    @Column(nullable = false)
    private long user2Score;

    public GameResults() {    }

    public GameResults(User user1, Long score1, User user2, Long score2) {
        setUser1(user1);
        setUser1Score(score1);
        setUser2(user2);
        setUser2Score(score2);
    }

    public User getUser1() {
        return new User(user1);
    }

    public void setUser1(User user1) {
        this.user1 = new User(user1);
    }

    public User getUser2() {
        return new User(user2);
    }

    public void setUser2(User user2) {
        this.user2 = new User(user2);
    }

    public long getUser1Score() {
        return user1Score;
    }

    public void setUser1Score(Long user1Score) {
        this.user1Score = user1Score;
    }

    public long getUser2Score() {
        return user2Score;
    }

    public void setUser2Score(Long user2Score) {
        this.user2Score = user2Score;
    }
}
