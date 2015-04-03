package gameConnectors;

/**
 * Created by mihanik on 31.03.15.
 */
interface Game {
    public void dispatchMessage(MyWebSocket sockFrom, String message);
    public void informClosed(MyWebSocket sockFrom);
}
