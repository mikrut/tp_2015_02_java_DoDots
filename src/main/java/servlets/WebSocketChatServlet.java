package servlets;

import gameConnectors.WebSocketFactory;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import user.AccountManager;

import javax.servlet.annotation.WebServlet;

/**
 * Created by mihanik on 31.03.15.
 */
@WebServlet(name = "WebSocketChatServlet", urlPatterns = {"/chat"})
public class WebSocketChatServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private final AccountManager manager;

    public WebSocketChatServlet(AccountManager man) {
        manager = man;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new WebSocketFactory(manager));
    }
}
