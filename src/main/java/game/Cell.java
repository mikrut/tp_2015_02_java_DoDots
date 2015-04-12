package game;

import user.User;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
class Cell {
    private User owner = null;
    private Boolean isVisited = false, isMarked = false;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        owner = user;
    }
;
    public void visit() {
        isVisited = true;
    }

    public void mark() {
        isMarked = true;
    }

    public void unvisit() {
        isVisited = false;
    }

    public void unmark() {
        isMarked = false;
    }

    public Boolean isVisited() {
        return isVisited;
    }

    public Boolean isMarked() {
        return isMarked;
    }

    public Boolean hasOwner() {
        return owner != null;
    }
}
