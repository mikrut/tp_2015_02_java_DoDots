package game;

import user.User;

/**
 * Created by mihanik on 31.03.15.
 */
public class Cell {
    private User owner = null;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        owner = user;
    }
}
