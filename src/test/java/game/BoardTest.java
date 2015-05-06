package game;

import junit.framework.TestCase;
import database.User;

public class BoardTest extends TestCase {
    private final User user = new User("admin", "admin", "admin", (long) 1);
    private final Board b = new Board(5, 5, user, user);

    private void capture(User user, Integer[][] array, Board board) {
        for(int i =0 ; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == 1)
                    board.capture(user, i, j);
            }
        }
    }

    public void testFindCycles() throws Exception {
        Integer[][] captureArray = {
                {1, 1, 1, 0, 0},
                {1, 0, 1, 0, 0},
                {1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        capture(user, captureArray, b);
        assertEquals("Expected inside cell to be captured by user", user, b.getOwner(1,1));
    }

    public void testFindCycles2() throws Exception {
        Integer[][] captureArray = {
                {1, 1, 1, 0, 0},
                {1, 0, 1, 0, 0},
                {1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        capture(user, captureArray, b);
        assertEquals("Expected inside cell to be captured by user", user, b.getOwner(1,1));
    }

    public void testFindCycles3() throws Exception {
        Integer[][] captureArray = {
                {1, 1, 1, 1, 0},
                {1, 0, 0, 1, 0},
                {1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        capture(user, captureArray, b);
        assertEquals("Expected inside cell to be captured by user", user, b.getOwner(1,1));
        assertEquals("Expected inside cell to be captured by user", user, b.getOwner(1,2));
    }

    public void testFindCycles4() throws Exception {
        Integer[][] captureArray = {
                {1, 1, 1, 1, 0},
                {1, 0, 0, 1, 0},
                {1, 1, 0, 1, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 1, 0, 0}
        };
        capture(user, captureArray, b);
        assertEquals("Expected inside cell to be captured by user", user, b.getOwner(3,2));
    }
}