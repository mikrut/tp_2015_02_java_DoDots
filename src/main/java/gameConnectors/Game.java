package gameConnectors;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
interface Game {
    public void dispatchMessage(MyWebSocket sockFrom, String message);
    public void informClosed(MyWebSocket sockFrom);
    public void replace(MyWebSocket replaced, MyWebSocket newSock);
}
