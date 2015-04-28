package gameConnectors;

import game.Board;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import resources.GameInfoResource;
import resources.ResourceProvider;
import resources.ResponseResource;
import user.DAManager;
import user.User;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
public class ClickGameImp implements  Game{
    private final Board board;
    private ResponseResource responseResource = null;
    private GameInfoResource setup = null;
    private boolean gameEnd = false;

    @SuppressWarnings("FieldCanBeLocal")
    private final MyWebSocket sock1;
    @SuppressWarnings("FieldCanBeLocal")
    private final MyWebSocket sock2;

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
        board = new Board(setup.getBoardSizeX(), setup.getBoardSizeY(), sock1.getClient(), sock2.getClient());
        response.put(setup.getFirstPlayerFieldCall(), false);
        sock2.sendMessage(response.toJSONString());
    }


    @Override
    @SuppressWarnings("unchecked")
    public void dispatchMessage(MyWebSocket sockFrom, String message) {
        JSONObject obj = (JSONObject) JSONValue.parse(message);
        Integer score1 = board.getScore(0);
        Integer score2 = board.getScore(1);

        if (obj == null) {
            obj = new JSONObject();
        }

        if (!gameEnd) {
            if (obj == null
                    || !obj.containsKey("row")
                    || !obj.containsKey("col")) {
                obj = new JSONObject();
                obj.put(responseResource.getStatus(), responseResource.getError());
                obj.put(responseResource.getMessage(), setup.getCommandInvalidMessage());
            } else {
                Integer row = Integer.parseInt(obj.get("row").toString());
                Integer col = Integer.parseInt(obj.get("col").toString());

                if (board.capture(sockFrom.getClient(), row, col)) {
                    obj.put(responseResource.getStatus(), responseResource.getOk());
                    obj.put(responseResource.getMessage(), setup.getCommandAcceptedMessage());

                    score1 = board.getScore(0);
                    score2 = board.getScore(1);
                    if (score1 + score2 == setup.getBoardSizeX() * setup.getBoardSizeY()) {
                        DAManager.getSingleton().getAccountManager().incScore(sock1.getClient(), score1);
                        DAManager.getSingleton().getAccountManager().incScore(sock2.getClient(), score2);
                        gameEnd = true;
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
        obj.put(setup.getBoardCall(), board.toJSONArray(sockFrom.getClient()));
        obj.put(setup.getWhoMovesCall(), board.getWhoMoves());

        JSONArray score = new JSONArray();
        score.add(0, score1);
        score.add(1, score2);
        obj.put(setup.getScoreCall(), score);
        obj.put(setup.getGameEndCall(), gameEnd);
        sock1.sendMessage(obj.toJSONString());
        sock2.sendMessage(obj.toJSONString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void informClosed(MyWebSocket sockFrom) {
        JSONObject obj = new JSONObject();
        obj.put(responseResource.getStatus(), setup.getConnectCloseStatus());
        obj.put(responseResource.getMessage(), setup.getConnectCloseMessage());
        sockFrom.sendMessage(obj.toJSONString());
    }
}
