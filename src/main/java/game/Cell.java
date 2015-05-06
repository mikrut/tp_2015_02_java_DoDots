package game;

import database.User;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
class Cell {
    private User owner = null;
    private Boolean isMarked = false;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        owner = user;
    }

    public void mark() {
        isMarked = true;
    }

    public void unmark() {
        isMarked = false;
    }

    public Boolean isMarked() {
        return isMarked;
    }

    @SuppressWarnings("UnusedDeclaration")
    public Boolean hasOwner() {
        return owner != null;
    }
}
