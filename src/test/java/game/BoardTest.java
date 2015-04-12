package game;

import junit.framework.TestCase;
import user.User;

public class BoardTest extends TestCase {
    User user = new User("admin", "admin", "admin", new Long(1));
    Board b = new Board(5, 5, user, user);

    public void testFindCycles() throws Exception {
        b.capture(user, 0, 0);
        b.capture(user, 0, 1);
        b.capture(user, 0, 2);

        b.capture(user, 1, 0);
        b.capture(user, 1, 2);

        b.capture(user, 2, 0);
        b.capture(user, 2, 1);
        b.capture(user, 2, 2);

        assertEquals("Expected inside cell to be captured by user", user, b.getOwner(1,1));
    }

    public void testFindCycles2() throws Exception {
        b.capture(user, 0, 0);
        b.capture(user, 0, 1);
        b.capture(user, 0, 2);

        b.capture(user, 1, 0);
        b.capture(user, 1, 2);

        b.capture(user, 2, 1);
        b.capture(user, 2, 2);

        assertEquals("Expected inside cell to be captured by user", user, b.getOwner(1,1));
    }

    public void testFindCycles3() throws Exception {
        b.capture(user, 0, 0);
        b.capture(user, 0, 1);
        b.capture(user, 0, 2);
        b.capture(user, 0, 3);

        b.capture(user, 1, 0);
        b.capture(user, 1, 3);

        b.capture(user, 2, 0);
        b.capture(user, 2, 1);
        b.capture(user, 2, 2);
        b.capture(user, 2, 3);

        assertEquals("Expected inside cell to be captured by user", user, b.getOwner(1,1));
        assertEquals("Expected inside cell to be captured by user", user, b.getOwner(1,2));
    }
}