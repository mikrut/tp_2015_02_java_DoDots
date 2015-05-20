package gameConnectors;

import game.Board;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import resources.GameInfoResource;
import resources.ResourceProvider;
import resources.ResponseResource;
import database.DAManager;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
public class ClickGameImp implements  Game{
    private final Board board;
    private ResponseResource responseResource = null;
    private GameInfoResource setup = null;

    private MyWebSocket sock1;
    private MyWebSocket sock2;

    public MyWebSocket getSock1() {
        return sock1;
    }

    public MyWebSocket getSock2() {
        return sock2;
    }

    @SuppressWarnings("unchecked")
    public ClickGameImp(MyWebSocket sock1, MyWebSocket sock2) {
        responseResource = (ResponseResource) ResourceProvider.getProvider().getResource("response_resource.xml");
        setup = (GameInfoResource) ResourceProvider.getProvider().getResource("game_info.xml");

        JSONObject response = new JSONObject();
        this.sock1 = sock1;
        sock1.setGame(this);
        this.sock2 = sock2;
        sock2.setGame(this);

        response.put(responseResource.getStatus(), setup.getGameStartStatus());
        response.put(responseResource.getMessage(), setup.getGameStartMessage());
        response.put(setup.getFirstPlayerFieldCall(), true);
        sock1.sendMessage(response.toJSONString());
        board = new Board(setup.getBoardSizeX(), setup.getBoardSizeY());
        response.put(setup.getFirstPlayerFieldCall(), false);
        sock2.sendMessage(response.toJSONString());
    }

    public synchronized void replace(MyWebSocket replaced, MyWebSocket newSock) {
        if (sock1 == replaced)
            sock1 = newSock;
        if (sock2 == replaced)
            sock2 = newSock;
        JSONObject obj = new JSONObject();
        obj.put(responseResource.getStatus(), responseResource.getOk());
        obj.put(responseResource.getMessage(), setup.getInfoMessage());
        generateStatus(obj);

        sock1.sendMessage(obj.toJSONString());
        sock2.sendMessage(obj.toJSONString());
    }

    private void generateStatus(JSONObject writer) {
        int score1 = board.getScore(0);
        int score2 = board.getScore(1);

        writer.put(setup.getBoardCall(), board.toJSONArray());
        writer.put(setup.getWhoMovesCall(), board.getWhoMoves());

        JSONArray score = new JSONArray();
        score.add(0, score1);
        score.add(1, score2);
        writer.put(setup.getScoreCall(), score);
        writer.put(setup.getGameEndCall(), board.isOver());
    }


    @Override
    @SuppressWarnings("unchecked")
    public synchronized void dispatchMessage(MyWebSocket sockFrom, String message) {
        JSONObject obj = (JSONObject) JSONValue.parse(message);
        Integer score1;
        Integer score2;

        if (obj == null) {
            obj = new JSONObject();
        }

        if (!board.isOver()) {
            if (!obj.containsKey("row") || !obj.containsKey("col")) {
                obj = new JSONObject();
                obj.put(responseResource.getStatus(), responseResource.getError());
                obj.put(responseResource.getMessage(), setup.getCommandInvalidMessage());
            } else {
                Integer row = Integer.parseInt(obj.get("row").toString());
                Integer col = Integer.parseInt(obj.get("col").toString());

                if (board.capture(sockFrom == sock1 ? 0 : 1, row, col)) {
                    obj.put(responseResource.getStatus(), responseResource.getOk());
                    obj.put(responseResource.getMessage(), setup.getCommandAcceptedMessage());

                    if (board.isOver()) {
                        score1 = board.getScore(0);
                        score2 = board.getScore(1);
                        DAManager.getSingleton().getUserDAO().incScore(sock1.getClient(), score1);
                        DAManager.getSingleton().getUserDAO().incScore(sock2.getClient(), score2);

                        DAManager.getSingleton().getGameResultsDAO().addResult(sock1.getClient(), score1.longValue(),
                                                                            sock2.getClient(), score2.longValue());
                    }

                } else {
                    obj.put(responseResource.getStatus(), responseResource.getError());
                    obj.put(responseResource.getMessage(), setup.getCommandInvalidMessage());
                }
            }

        } else {
            obj.put(responseResource.getStatus(), setup.getGameEndStatus());
            obj.put(responseResource.getMessage(), setup.getGameEndMessage());
        }
        generateStatus(obj);

        sock1.sendMessage(obj.toJSONString());
        sock2.sendMessage(obj.toJSONString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void informClosed(MyWebSocket sockFrom) {
        JSONObject obj = new JSONObject();
        MyWebSocket sockTo;
        if (sockFrom == sock1)
            sockTo = sock2;
        else
            sockTo = sock1;
        sock1 = sock2 = null;
        if (sockTo != null) {
            obj.put(responseResource.getStatus(), setup.getConnectCloseStatus());
            obj.put(responseResource.getMessage(), setup.getConnectCloseMessage());
            sockTo.sendMessage(obj.toJSONString());
            sockTo.close();
        }
    }
}
