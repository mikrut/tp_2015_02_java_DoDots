package gameConnectors;

import org.eclipse.jetty.websocket.api.Session;
import user.User;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

/**
 * Created by mihanik on 31.03.15.
 */

@WebSocket
public class WebSocketImp implements MyWebSocket {
    private User client;
    private Session session;
    private Game game;

    public WebSocketImp(User client) {
        this.client = client;
    }

    @Override
    public void sendMessage(String message) {
        try {
            session.getRemote().sendString(message);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        if(game!=null)
            game.dispatchMessage(this, message);
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        try {
            session.getRemote().sendString("Vse...");
        } catch (Exception e) {

        }
        if(game != null)
            game.informClosed(this);
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public User getClient() {
        return client;
    }
}
