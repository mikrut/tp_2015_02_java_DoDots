package gameConnectors;

/**
 * Created by mihanik on 31.03.15.
 */
public interface Game {
    public void dispatchMessage(MyWebSocket sockFrom, String message);
    public void informClosed(MyWebSocket sockFrom);
}
