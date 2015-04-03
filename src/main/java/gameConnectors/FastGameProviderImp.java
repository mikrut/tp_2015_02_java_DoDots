package gameConnectors;

/**
 * Created by mihanik on 31.03.15.
 */
public class FastGameProviderImp implements GameProvider {
    private MyWebSocket  socket = null;

    @Override
    public void addWebSocket(MyWebSocket sock) {
        if (socket == null) {
            socket = sock;
            System.out.println("Added sock");
            sock.sendMessage("I see you!");
        } else {
            System.out.println("new game");
            new ClickGameImp(socket, sock);//new ChatGameImp(socket, sock);
            socket = null;
        }
    }
}
