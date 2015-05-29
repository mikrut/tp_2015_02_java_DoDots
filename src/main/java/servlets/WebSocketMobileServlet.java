package servlets;

import gameConnectors.MobileWebSocketFactory;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * Created by mihanik
 * 27.05.15 23:40
 * Package: servlets
 */

@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/game"})
public class WebSocketMobileServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;

    public WebSocketMobileServlet() {}

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new MobileWebSocketFactory());
    }
}
