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
            System.out.println("Added sock");
            sock.sendMessage("I see you!");
        } else {
            System.out.println("new game");
            game = new ClickGameImp(socket, sock);//new ChatGameImp(socket, sock);
            socket = null;
        }
    }
}
