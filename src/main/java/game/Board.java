package game;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.User;

/**
 * Created by mihanik on 31.03.15.
 */
public class Board {
    private Cell cells[][] = null;
    private Integer rowSize, colSize;

    public Board(Integer rows, Integer cols) {
        rowSize = rows;
        colSize = cols;
        cells = new Cell[rows][cols];
        for(Integer i = 0; i<rowSize; i++)
            for(Integer j =0; j<colSize; j++)
                cells[i][j] = new Cell();
    }


    public Boolean capture(User user, Integer row, Integer col) {
        if (row < 0 || row >= rowSize || col < 0 || col>= rowSize || cells[row][col].getOwner() != null) {
            return false;
        } else {
            cells[row][col].setOwner(user);
            return true;
        }
    }

    public JSONArray toJSONArray() {
        JSONArray arr = new JSONArray();
        for(Integer i = 0; i<rowSize; i++) {
            JSONArray row = new JSONArray();
            for (Integer j = 0; j<colSize; j++) {
                row.add(j, (cells[i][j].getOwner() != null) ? 1 : 0);
            }
            arr.add(i, row);
        }
        return arr;
    }
}
