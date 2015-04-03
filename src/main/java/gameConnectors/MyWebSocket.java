package gameConnectors;

import org.eclipse.jetty.websocket.api.Session;
import user.User;

/**
 * Created by mihanik on 31.03.15.
 */
interface MyWebSocket {
    public void sendMessage(String message);
    public User getClient();
    public void setGame(Game g);
    public Session getSession();
}
