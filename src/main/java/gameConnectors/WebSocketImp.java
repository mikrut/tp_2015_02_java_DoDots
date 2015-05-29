package gameConnectors;

import org.eclipse.jetty.websocket.api.Session;
import database.User;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
@WebSocket
public class WebSocketImp implements MyWebSocket {
    private final User client;
    private Session session;
    private Game game;
    private GameProvider provider;

    public WebSocketImp(User client) {
        this.client = client;
    }

    @Override
    public void sendMessage(String message) {
        try {
            if(session != null && session.isOpen())
                session.getRemote().sendString(message);
        } catch (Exception e) {
            close();
            System.out.println("Error in sendMessage: " + e.toString());
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @OnWebSocketMessage
    public void onMessage(String message) {
        if(game!=null)
            game.dispatchMessage(this, message);
    }

    @SuppressWarnings("UnusedDeclaration")
    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
        if (provider != null)
            provider.addWebSocket(this);
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    @SuppressWarnings({"UnusedDeclaration", "UnusedParameters"})
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        if(game != null)
            game.informClosed(this);
    }

    public void close() {
        if (session != null)
            session.close();
        if (provider != null)
            provider.informClosed(this);
        if (game != null)
            game.informClosed(this);
        provider = null;
        game     = null;
        session = null;
    }



    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setProvider(GameProvider provider) {
        this.provider = provider;
    }

    @Override
    public User getClient() {
        return client;
    }
}
