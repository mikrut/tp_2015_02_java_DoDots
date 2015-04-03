package gameConnectors;

import game.Board;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by mihanik on 31.03.15.
 */
public class ClickGameImp implements  Game{
    private final Board board = new Board(5,5);
    private final MyWebSocket sock1;
    private final MyWebSocket sock2;

    public ClickGameImp(MyWebSocket sock1, MyWebSocket sock2) {
        this.sock1 = sock1;
        sock1.setGame(this);
        this.sock2 = sock2;
        sock2.setGame(this);


        JSONObject obj = new JSONObject();
        obj.put("status", "OK");
        obj.put("message", "Connection established");

        this.sock1.sendMessage(obj.toJSONString());
        this.sock2.sendMessage(obj.toJSONString());
    }


    @Override
    public void dispatchMessage(MyWebSocket sockFrom, String message) {
        JSONObject obj = (JSONObject) JSONValue.parse(message);
        if (obj == null
            || !obj.containsKey("row")
            || !obj.containsKey("col")) {
            obj = new JSONObject();
            obj.put("status", "Error");
            obj.put("message", "Invalid data!");
            obj.put("board", board.toJSONArray());
            sockFrom.sendMessage(obj.toJSONString());
        } else {
            Integer row = Integer.parseInt(obj.get("row").toString());
            Integer col = Integer.parseInt(obj.get("col").toString());
            if (board.capture(sockFrom.getClient(), row, col)) {
                obj.put("status", "OK");
                obj.put("message", "Data accepted");
                obj.put("board", board.toJSONArray());
                sockFrom.sendMessage(obj.toJSONString());
            } else {
                obj.put("status", "Error");
                obj.put("message", "Invalid data!");
                obj.put("board", board.toJSONArray());
                sockFrom.sendMessage(obj.toJSONString());
            }
        }
    }

    @Override
    public void informClosed(MyWebSocket sockFrom) {
        JSONObject obj = new JSONObject();
        obj.put("status", "Close");
        obj.put("message", "Connection closed");
        sockFrom.sendMessage(obj.toJSONString());
    }
}
