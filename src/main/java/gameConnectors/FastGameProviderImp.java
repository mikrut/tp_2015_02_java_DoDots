package gameConnectors;

import org.json.simple.JSONObject;
import resources.GameProviderResource;
import resources.ResourceProvider;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
public class FastGameProviderImp implements GameProvider {
    private MyWebSocket  socket = null;
    private final GameProviderResource setup = (GameProviderResource) ResourceProvider.getProvider().getResource("gameprovider.xml");

    @SuppressWarnings("unchecked")
    @Override
    public void addWebSocket(MyWebSocket sock) {
        JSONObject response = new JSONObject();
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
