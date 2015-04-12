package resources;

import java.util.IntSummaryStatistics;

/**
 * Created by mihanik
 * 07.04.15 9:30
 * Package: resources
 */

public class GameInfoResource implements Resource {
    private String connectCloseStatus;
    private String connectCloseMessage;
    private String commandAcceptedMessage;
    private String commandInvalidMessage;
    private String boardCall;
    private String gameStartStatus;
    private String gameStartMessage;
    private String firstPlayerFieldCall;
    private String whoMovesCall;

    private Integer boardSizeX;
    private Integer boardSizeY;

    public String getConnectCloseStatus() {
        return connectCloseStatus;
    }

    public String getConnectCloseMessage() {
        return connectCloseMessage;
    }

    public String getCommandAcceptedMessage() {
        return commandAcceptedMessage;
    }

    public String getCommandInvalidMessage() {
        return commandInvalidMessage;
    }

    public String getBoardCall() {
        return boardCall;
    }

    public String getGameStartStatus() {
        return gameStartStatus;
    }

    public String getGameStartMessage() {
        return gameStartMessage;
    }

    public String getFirstPlayerFieldCall() {
        return firstPlayerFieldCall;
    }

    public String getWhoMovesCall() {
        return whoMovesCall;
    }

    public Integer getBoardSizeX() {
        return boardSizeX;
    }

    public  Integer getBoardSizeY() {
        return boardSizeY;
    }
}
