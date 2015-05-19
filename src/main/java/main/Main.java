/**
 * Created by Михаил
 * 28.02.2015 9:15
 * Package: ${PACKAGE_NAME}
 */
package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import resources.ResourceProvider;
import resources.ServerPathResource;
import servlets.*;
import user.*;

import javax.servlet.Servlet;

class Main {
    public static void main(String[] args) throws Exception {
        int port = ((ServerPathResource) ResourceProvider.getProvider().getResource("server_path.xml")).getDefaultPort();
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server(port);
        AccountManager mgr = new DBAccountManager();

        Servlet register = new RegisterServlet(mgr);
        Servlet login    = new LoginServlet(mgr);
        Servlet logout   = new LogoutServlet(mgr);
        Servlet userInfo = new UserInfoServlet(mgr);
        Servlet admin    = new AdminServlet(server, mgr);
        Servlet game     = new WebSocketGameServlet(mgr);

        ServerPathResource paths = (ServerPathResource) ResourceProvider.getProvider().getResource("server_path.xml");

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(register), paths.getSigninUrl());
        context.addServlet(new ServletHolder(login),    paths.getLoginUrl());
        context.addServlet(new ServletHolder(logout),   paths.getLogoutUrl());
        context.addServlet(new ServletHolder(userInfo), paths.getUserInfoUrl());
        context.addServlet(new ServletHolder(admin),    paths.getAdminInfoUrl());
        context.addServlet(new ServletHolder(game),     paths.getWebSocketUrl());

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase(paths.getStaticDir());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});


        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
