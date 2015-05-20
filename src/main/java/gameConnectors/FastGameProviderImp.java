package gameConnectors;

import database.User;
import org.json.simple.JSONObject;
import resources.GameProviderResource;
import resources.ResourceProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
public class FastGameProviderImp implements GameProvider {
    private MyWebSocket  socket = null;
    private Map<User, MyWebSocket> socketMap = new HashMap<>();
    private final GameProviderResource setup = (GameProviderResource) ResourceProvider.getProvider().getResource("gameprovider.xml");

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void addWebSocket(MyWebSocket sock) {
        JSONObject response = new JSONObject();
        if (socketMap.containsKey(sock.getClient())) {
            MyWebSocket replaced = socketMap.get(sock.getClient());
            if (socket == replaced)
                socket = sock;
            Game g = replaced.getGame();
            if (g != null)
                g.replace(replaced, sock);
            socketMap.put(sock.getClient(), sock);
        } else {
            socketMap.put(sock.getClient(), sock);
            if (socket == null) {
                socket = sock;
                response.put("Status", setup.getConnectSuccessStatus());
                response.put("Message", setup.getConnectSuccessMessage());
                sock.sendMessage(response.toJSONString());
            } else {
                response.put("Status", setup.getConnectSuccessStatus());
                response.put("Message", setup.getConnectSuccessMessage());
                sock.sendMessage(response.toJSONString());
                response.clear();
                new ClickGameImp(socket, sock);
                socket = null;
            }
        }
    }

    @Override
    public synchronized void informClosed(MyWebSocket sock) {
        socketMap.remove(sock.getClient(), sock);
        if (socket == sock)
            socket = null;
    }
}
