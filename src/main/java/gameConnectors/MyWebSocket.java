package gameConnectors;

import org.eclipse.jetty.websocket.api.Session;
import database.User;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
interface MyWebSocket {
    public void sendMessage(String message);
    public User getClient();
    public void setGame(Game g);
    public Game getGame();
    public void close();
    @SuppressWarnings("UnusedDeclaration")
    public Session getSession();
    public void setProvider(GameProvider p);
}
