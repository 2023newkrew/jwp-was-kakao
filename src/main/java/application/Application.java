package application;

import application.controller.RootController;
import application.controller.UserController;
import webserver.WebServer;
import webserver.handler.Handlers;
import webserver.resolver.Resolvers;
import webserver.resolver.ResourceHandler;
import webserver.resolver.ResourceResolver;
import webserver.resolver.ViewResolver;

public class Application {

    private static final int DEFAULT_PORT = 8080;

    private static final Resolvers resolvers = new Resolvers(
            new ResourceResolver(),
            new ViewResolver()
    );
    private static final Handlers handlers = new Handlers(
            new RootController(),
            new UserController(),
            new ResourceHandler(resolvers)
    );

    public static void main(String[] args) {
        new WebServer(handlers).listen(DEFAULT_PORT);
    }
}
