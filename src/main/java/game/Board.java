package game;

import javafx.util.Pair;
import org.json.simple.JSONArray;

import java.util.Stack;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
public class Board {
    private Cell cells[][] = null;
    private boolean gameOver = false;
    private final int rowSize;
    private final int colSize;
    private int whoMoves = 0;
    private int[] userClickable = new int[2];
    private final Integer[] score = new Integer[2];

    public Board(Integer rows, Integer cols) {
        rowSize = rows;
        colSize = cols;
        cells = new Cell[rows][cols];
        for(int i = 0; i < rowSize; i++)
            for(int j = 0; j < colSize; j++)
                cells[i][j] = new Cell();

        userClickable[0] = userClickable[1] = rows * cols;
        score[0] = score[1] = 0;
    }

    public boolean isOver() {
        return gameOver;
    }


    public boolean capture(int userIndex, Integer row, Integer col) {
        if (userIndex == whoMoves)
            return captureNoTurnCheck(userIndex, row, col);
        return false;
    }

    public boolean captureNoTurnCheck(int userIndex, Integer row, Integer col) {
        if (!gameOver &&
                row >= 0 && row < rowSize &&
                col >= 0 && col < rowSize) {
            Cell.State previous = cells[row][col].getState();
            if (cells[row][col].captureUsual(userIndex)) {
                countScore(previous, cells[row][col], userIndex);

                Cell.State current = cells[row][col].getState();
                if (current == Cell.State.FIRST_OWNED ||
                        current == Cell.State.SECOND_OWNED)
                    findCycles(row, col, userIndex);

                if (userClickable[1 - userIndex] > 0) {
                    whoMoves = 1 - userIndex;
                } else {
                    gameOver = true;
                    finalCount();
                }
                return true;
            }
        }
        return false;
    }

    void finalCount() {
        for(int i = 0; i < rowSize; i++)
            for(int j = 0; j < colSize; j++) {
                Cell.State previous = cells[i][j].getState();
                Integer index = cells[i][j].finalCapture();
                if (index != null)
                    countScore(previous, cells[i][j], index);
            }
    }

    void countScore(Cell.State previous, Cell cell, int capturerIndex) {
        switch (cell.getState()) {
            case CAPTURED_BY_FIRST:
            case CAPTURED_BY_SECOND:
                score[capturerIndex]++;
        }
        if (cell.isFreeToCapture(cell.getState())) {
            userClickable[0]++;
            userClickable[1]++;
        }
        switch (previous) {
            case CAPTURED_BY_FIRST:
            case CAPTURED_BY_SECOND:
                score[1 - capturerIndex]--;
        }
        if (cell.isFreeToCapture(previous)) {
            userClickable[0]--;
            userClickable[1]--;
        }
    }

    public void captureMandatory(int userIndex, int row, int col) {
        Cell.State previous = cells[row][col].getState();
        cells[row][col].setState(
                Cell.State.values()[
                        Cell.State.FIRST_OWNED.ordinal() + userIndex
                        ]
        );
        countScore(previous, cells[row][col], userIndex);
    }

    void captureInCycle(int userIndex, int row, int col) {
        Cell.State previous = cells[row][col].getState();
        cells[row][col].captureAround(userIndex);
        countScore(previous, cells[row][col], userIndex);
    }

    @SuppressWarnings("unchecked")
    public JSONArray toJSONArray() {
        JSONArray arr = new JSONArray();
        for(Integer i = 0; i<rowSize; i++) {
            JSONArray row = new JSONArray();
            for (Integer j = 0; j<colSize; j++)
                row.add(j, cells[i][j].getState().ordinal());
            arr.add(i, row);
        }
        return arr;
    }

    public void setCellType(int type, int row, int col) {
        cells[row][col].setState(type);
    }

    public Cell.State getCellType(int row, int col) {
        return cells[row][col].getState();
    }

    public int getWhoMoves() {
        return whoMoves;
    }

    public int getScore(Integer index) {
        return score[index];
    }

    void unmarkAll() {
        for(int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                cells[i][j].unmark();
            }
        }
    }

    void findCycles(Integer row, Integer col, int userIndex) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if (!(i==0 && j==0) &&
                        cellExists(row+i, col+j) &&
                        cells[row+i][col+j].canMove(userIndex)) {
                    tryInsideCycle(row+i, col+j, userIndex);
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    void tryInsideCycle(Integer row, Integer col, int userIndex) {
        Stack<Pair<Integer, Integer>> toVisit = new Stack<>(), toCapture = new Stack<>();
        int curRow, curCol;
        Pair<Integer, Integer> current;
        Boolean finish = false;
        unmarkAll();

        toVisit.push(new Pair(row, col));
        toCapture.add(new Pair(row, col));
        cells[row][col].mark();
        while(!finish && !toVisit.empty()) {
            current = toVisit.pop();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if(i*j == 0) { // Мы можем перемещаться только по прямой, но не по диагонали
                        curRow = current.getKey() + i;
                        curCol = current.getValue() + j;
                        if (isBorder(curRow, curCol) && cells[curRow][curCol].canMove(userIndex)) {
                            finish = true;
                        } else if (insideBorderCell(curRow, curCol) &&
                                cells[curRow][curCol].canMove(userIndex) &&
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
                captureInCycle(userIndex, current.getKey(), current.getValue());
            }
        }
    }

    boolean cellExists(int row, int col) {
        return (row >= 0 && row < rowSize &&
                col >= 0 && col < colSize &&
                cells[row][col] != null);
    }

    boolean isBorder(int row, int col) {
        return  (row == 0 || row == rowSize -1 ||
                col == 0 || col == colSize -1) &&
                row >= 0 && row <= rowSize -1 &&
                col >= 0 && col <= colSize -1;
    }

    boolean insideBorderCell(int row, int col) {
        return  row > 0 && row < rowSize -1 &&
                col > 0 && col < colSize -1 &&
                cells[row][col] != null;
    }
}
