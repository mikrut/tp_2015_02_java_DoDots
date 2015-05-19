package game;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
    private final Board b = new Board(5, 5);

    private void capture(Integer[][] array, Board board) {
        for(int i =0 ; i < array.length; i++)
            for (int j = 0; j < array[i].length; j++)
                if (array[i][j] != 0)
                board.captureNoTurnCheck(array[i][j] - 1, i, j);
    }

    @Test
    public void testCapture() {
        b.capture(0, 0, 0);
        assertEquals("Expected cell to be owned by user 0", Cell.State.FIRST_OWNED, b.getCellType(0,0));
    }

    @Test
    public void testChangeTurn() {
        int turner = b.getWhoMoves();
        b.capture(turner, 0, 0);
        assertNotEquals("Expected turner to change", turner, b.getWhoMoves());
    }

    @Test
    public void testGameOver() {
        Integer[][] captureArray = {
                {1, 1, 1, 1, 1},
                {1, 2, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}
        };
        capture(captureArray, b);
        assertTrue("Expected game to be over because of full field", b.isOver());
    }

    @Test
    public void testGameNotOver() {
        Integer[][] captureArray = {
                {1, 1, 1, 1, 1},
                {1, 2, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 0}
        };
        capture(captureArray, b);
        assertFalse("Expected game to be not over because of free cell", b.isOver());
    }

    @Test
    public void testScore() {
        Integer[][] captureArray = {
                {1, 1, 1, 0, 0},
                {1, 2, 1, 0, 0},
                {1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        capture(captureArray, b);
        assertEquals("Expected user 0 score to be 1", 1, b.getScore(0));
    }

    @Test
    public void testFindCycles() throws Exception {
        Integer[][] captureArray = {
                {1, 1, 1, 0, 0},
                {1, 0, 1, 0, 0},
                {1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        capture(captureArray, b);
        assertEquals("Expected inside cell to be occupied by user", Cell.State.OCCUPIED_BY_FIRST, b.getCellType(1,1));
    }

    @Test
    public void testFindCycles2() throws Exception {
        Integer[][] captureArray = {
                {1, 1, 1, 0, 0},
                {1, 0, 1, 0, 0},
                {1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        capture(captureArray, b);
        assertEquals("Expected inside cell to be occupied by user", Cell.State.OCCUPIED_BY_FIRST, b.getCellType(1,1));
    }

    @Test
    public void testFindCycles3() throws Exception {
        Integer[][] captureArray = {
                {1, 1, 1, 1, 0},
                {1, 0, 0, 1, 0},
                {1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        capture(captureArray, b);
        assertEquals("Expected inside cell to be occupied by user", Cell.State.OCCUPIED_BY_FIRST, b.getCellType(1, 1));
        assertEquals("Expected inside cell to be occupied by user", Cell.State.OCCUPIED_BY_FIRST, b.getCellType(1, 2));
    }

    @Test
    public void testFindCycles4() throws Exception {
        Integer[][] captureArray = {
                {1, 1, 1, 1, 0},
                {1, 0, 0, 1, 0},
                {1, 1, 0, 1, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 1, 0, 0}
        };
        capture(captureArray, b);
        assertEquals("Expected inside cell to be occupied by user", Cell.State.OCCUPIED_BY_FIRST, b.getCellType(3, 2));
    }
}