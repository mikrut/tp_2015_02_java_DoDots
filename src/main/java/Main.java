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

import javax.servlet.Servlet;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Servlet register = new RegisterServlet();
        Servlet login = new LoginServlet();
        Servlet logout = new LogoutServlet();
        Servlet userinfo = new UserinfoServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(register), "/signin");
        context.addServlet(new ServletHolder(login), "/login");
        context.addServlet(new ServletHolder(logout), "/logout");
        context.addServlet(new ServletHolder(userinfo), "/getinfo");
        context.addServlet(new ServletHolder(new AdminServlet()), "/getadmin");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
