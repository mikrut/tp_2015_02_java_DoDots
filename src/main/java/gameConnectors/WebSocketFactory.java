package gameConnectors;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import user.AccountManager;
import user.User;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mihanik on 31.03.15.
 */
public class WebSocketFactory implements WebSocketCreator {
    private AccountManager manager;
    private GameProvider prov;

    public WebSocketFactory(AccountManager manager) {
        prov = new FastGameProviderImp();
        this.manager = manager;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        User usr = null;
        if((request != null) && request.getSession()!=null)
            usr = manager.getAuthenticated(request.getSession().getId());
        MyWebSocket sock = new WebSocketImp(usr);
        prov.addWebSocket(sock);
        return sock;
    }
}
