package gameConnectors;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
@SuppressWarnings("UnusedDeclaration")
public class ChatGameImp implements Game {
    private MyWebSocket sock1, sock2;

    public ChatGameImp(MyWebSocket sock1, MyWebSocket sock2){
        this.sock1 = sock1;
        sock1.setGame(this);
        this.sock2 = sock2;
        sock2.setGame(this);

        this.sock1.sendMessage("Connection open");
        this.sock2.sendMessage("Connection open");
    }

    @Override
    public void dispatchMessage(MyWebSocket sockFrom, String message) {
        if(sockFrom == sock1)
            sock2.sendMessage(message);
        else if (sockFrom == sock2)
            sock1.sendMessage(message);
    }

    @Override
    public void informClosed(MyWebSocket sockFrom) {
        sock1.sendMessage("Connection closed");
        sock2.sendMessage("Connection closed");
        sock1 = null;
        sock2 = null;
    }
}
