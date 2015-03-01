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

import servlets.TemplateServlet;
import javax.servlet.Servlet;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Servlet templator = new TemplateServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(templator), "/");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
