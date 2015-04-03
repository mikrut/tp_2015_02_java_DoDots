/**
 * Created by Михаил on 28.02.2015.
 */
package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import servlets.*;
import user.AccountManager;
import user.MapAccountManager;

import javax.servlet.Servlet;

class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server(port);
        AccountManager mgr = MapAccountManager.getManager();

        Servlet register = new RegisterServlet(mgr);
        Servlet login = new LoginServlet(mgr);
        Servlet logout = new LogoutServlet(mgr);
        Servlet userinfo = new UserinfoServlet(mgr);
        Servlet admin = new AdminServlet(server, mgr);
        Servlet chat = new WebSocketChatServlet(mgr);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(register), "/signin");
        context.addServlet(new ServletHolder(login), "/login");
        context.addServlet(new ServletHolder(logout), "/logout");
        context.addServlet(new ServletHolder(userinfo), "/getinfo");
        context.addServlet(new ServletHolder(admin), "/getadmin/*");
        context.addServlet(new ServletHolder(chat), "/chat");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});


        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
