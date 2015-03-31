package gameConnectors;

import java.util.Queue;

/**
 * Created by mihanik on 31.03.15.
 */
public class FastGameProviderImp implements GameProvider {
    MyWebSocket  socket = null;

    @Override
    public void addWebSocket(MyWebSocket sock) {
        Game game;
        if (socket == null) {
            socket = sock;
        } else {
            game = new ChatGameImp(socket, sock);
        }
    }
}
