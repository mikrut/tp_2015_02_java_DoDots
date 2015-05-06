package gameConnectors;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import user.AccountManager;
import database.User;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
public class WebSocketFactory implements WebSocketCreator {
    private final AccountManager manager;
    private final GameProvider prov;

    public WebSocketFactory(AccountManager manager) {
        prov = new FastGameProviderImp();
        this.manager = manager;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        User usr = null;
        if((request != null) && request.getSession()!=null)
            usr = manager.getAuthenticated(request.getSession().getId());
        if (usr != null) {
            MyWebSocket sock = new WebSocketImp(usr);
            sock.setProvider(prov);
            return sock;
        } else {
            return null;
        }
    }
}
