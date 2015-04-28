package game;

import com.sun.javafx.geom.Vec3d;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import user.User;

import java.util.Stack;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
public class Board {
    private Cell cells[][] = null;
    private final Integer rowSize;
    private final Integer colSize;
    private User users[] = new User[2];
    private Integer whoMoves = 0, score[] = new Integer[2];

    public Board(Integer rows, Integer cols, User u1, User u2) {
        rowSize = rows;
        colSize = cols;
        cells = new Cell[rows][cols];
        for(Integer i = 0; i<rowSize; i++)
            for(Integer j =0; j<colSize; j++)
                cells[i][j] = new Cell();
        users[0]  = u1;
        users[1]  = u2;

        score[0] = 0;
        score[1] = 0;
    }


    public Boolean capture(User user, Integer row, Integer col) {
        if (user != users[whoMoves] || row < 0 || row >= rowSize || col < 0 || col>= rowSize || cells[row][col].getOwner() != null) {
            return false;
        } else {
            score[whoMoves]++;
            whoMoves = 1 - whoMoves;

            cells[row][col].setOwner(user);
            findCycles(row, col, user);
            return true;
        }
    }

    protected  void captureObligatory(User user, Integer row, Integer col) {
        if (cells[row][col].getOwner() != user && cells[row][col].getOwner() != null) {
            score[1-whoMoves]--;
        }
        score[whoMoves]++;
        cells[row][col].setOwner(user);
    }

    @SuppressWarnings("unchecked")
    public JSONArray toJSONArray(User userFor) {
        JSONArray arr = new JSONArray();
        for(Integer i = 0; i<rowSize; i++) {
            JSONArray row = new JSONArray();
            for (Integer j = 0; j<colSize; j++) {
                if(cells[i][j].getOwner() != null) {
                    if (cells[i][j].getOwner() == userFor) {
                        row.add(j, 1);
                    } else {
                        row.add(j, 2);
                    }
                } else {
                    row.add(j, 0);
                }
            }
            arr.add(i, row);
        }
        return arr;
    }

    public Integer getWhoMoves() {
        return whoMoves;
    }

    public Integer getScore(Integer index) {
        return score[index];
    }

    protected void unvisitAll() {
        for(int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                cells[i][j].unvisit();
                cells[i][j].unmark();
            }
        }
    }

    protected void findCycles(Integer row, Integer col, User user) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if (row+i>=0 && row+i<rowSize &&
                        col+j >= 0 && col+j < colSize &&
                        !(i==0 && j==0) && cells[row+i][col+j].getOwner() != user) {
                    tryInsideCycle(row+i, col+j, user);
                }
            }
        }

    }

    protected void tryInsideCycle(Integer row, Integer col, User user) {
        Stack<Pair<Integer, Integer>> toVisit = new Stack<>(), toCapture = new Stack<>();
        int curRow, curCol;
        Pair<Integer, Integer> current;
        Boolean finish = false;
        unvisitAll();

        toVisit.push(new Pair(row, col));
        toCapture.add(new Pair(row, col));
        cells[row][col].mark();
        while(!finish && !toVisit.empty()) {
            current = toVisit.pop();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if(i*j==0) { // Мы можем перемещаться только по прямой, но не по диагонали
                        curRow = current.getKey() + i;
                        curCol = current.getValue() + j;
                        if (((curRow == 0) || (curRow == rowSize - 1) ||
                                (curCol == 0) || (curCol == colSize - 1)) && getOwner(curRow, curCol) != user) {
                            finish = true;
                        } else if (curRow > 0 && curRow < rowSize - 1 &&
                                curCol > 0 && curCol < colSize - 1 &&
                                cells[curRow][curCol].getOwner() != user &&
                                !cells[curRow][curCol].isMarked()) {
                            toVisit.push(new Pair(curRow, curCol));
                            toCapture.push(new Pair(curRow, curCol));
                            cells[curRow][curCol].mark();
                        }
                    }
                }
            }
        }
        if(!finish) {
            while(!toCapture.empty()) {
                current = toCapture.pop();
                captureObligatory(user, current.getKey(), current.getValue());
            }
        }
    }

    protected Boolean insider(Integer row, Integer col, User user) {
        Integer counter = 0;
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if (row+i>=0 && row+i<rowSize &&
                        col+j >= 0 && col+j < colSize &&
                        !(i==0 && j==0) && cells[i][j].hasOwner()) {
                    counter++;
                }
            }
        }
        // Если все 8 окружающих ячеек захвачены текущим ходящим юзером,
        // то мы попали во внутреннюю область
        return counter == 8;
    }

    public User getOwner(Integer row, Integer col) {
        if(row<0 || row>=rowSize || col<0 || col>=colSize)
            return null;
        return cells[row][col].getOwner();
    }
}
