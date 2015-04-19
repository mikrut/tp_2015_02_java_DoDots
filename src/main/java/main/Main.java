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

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import servlets.*;
import user.AccountManager;
import user.DAOAccountManager;
import user.MapAccountManager;
import user.User;

import javax.servlet.Servlet;

class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server(port);
        AccountManager mgr = new DAOAccountManager(getFactory());

        Servlet register = new RegisterServlet(mgr);
        Servlet login = new LoginServlet(mgr);
        Servlet logout = new LogoutServlet(mgr);
        Servlet userInfo = new UserInfoServlet(mgr);
        Servlet admin = new AdminServlet(server, mgr);
        Servlet chat = new WebSocketChatServlet(mgr);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(register), "/signin");
        context.addServlet(new ServletHolder(login), "/login");
        context.addServlet(new ServletHolder(logout), "/logout");
        context.addServlet(new ServletHolder(userInfo), "/getinfo");
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

    private static org.hibernate.SessionFactory getFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);

        configuration.setProperty("hibernate.dialect",                 "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url",          "jdbc:mysql://localhost:3306/tp_dodots_production");
        configuration.setProperty("hibernate.connection.username",     "root");
        configuration.setProperty("hibernate.connection.password",     "0000");
        configuration.setProperty("hibernate.show_sql",                "true");
        configuration.setProperty("hibernate.hbm2ddl.auto",            "update");
        configuration.setProperty("hibernate.flushMode",               "COMMIT");

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry registry = builder.build();

        return configuration.buildSessionFactory(registry);
    }
}
