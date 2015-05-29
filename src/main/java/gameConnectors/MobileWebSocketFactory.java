package gameConnectors;

import database.User;
import javafx.util.Pair;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import java.security.SecureRandom;

/**
 * Created by mihanik
 * 27.05.15 23:12
 * Package: gameConnectors
 */
public class MobileWebSocketFactory implements WebSocketCreator, GameProvider {
    private Map<String, MyWebSocket> desktopWaiters = new HashMap<>();

    public MobileWebSocketFactory() {
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request,
                                  ServletUpgradeResponse response) {

        if((request != null) && request.getSession()!=null) {
            HttpServletRequest httpRequest = request.getHttpServletRequest();
            String isMobile = httpRequest.getParameter("isMobile");
            if (isMobile == null || !isMobile.equals("true")) {
                String token = request.getSession().getId();
                if (desktopWaiters.containsKey(token)) {
                    MyWebSocket waiter = desktopWaiters.get(token);
                    desktopWaiters.remove(token);
                    waiter.close();
                }
                MyWebSocket desktop = new WebSocketImp(null);
                desktop.setProvider(this);

                desktopWaiters.put(token, desktop);
                return desktop;
            } else {
                String token = httpRequest.getParameter("token");
                if (desktopWaiters.containsKey(token)) {
                    MyWebSocket mobile = new WebSocketImp(null);
                    MyWebSocket desktop = desktopWaiters.get(token);
                    desktopWaiters.remove(token);
                    mobile.setProvider(this);

                    new ChatGameImp(mobile, desktop);
                    return mobile;
                } else {
                    return null;
                }
            }
        }

        return null;
    }

    public void addWebSocket(MyWebSocket sock){}

    public void informClosed(MyWebSocket sock){
        if (desktopWaiters.containsValue(sock)) {
            String token = null;
            for(Map.Entry<String, MyWebSocket> entry: desktopWaiters.entrySet()) {
                if(entry.getValue() == sock)
                    token = entry.getKey();
            }
            desktopWaiters.remove(token);
        }
    }
}
