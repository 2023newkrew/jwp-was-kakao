package application;

import application.controller.RootController;
import application.controller.UserController;
import application.db.DataBase;
import application.enums.ApplicationContentType;
import application.model.User;
import webserver.WebServer;
import webserver.handler.Handlers;
import webserver.handler.resolver.statics.StaticResolver;
import webserver.handler.resolver.statics.StaticType;
import webserver.handler.resolver.statics.StaticTypes;
import webserver.handler.resolver.view.ViewResolver;
import webserver.handler.resource.ResourceHandler;

public class Application {

    private static final int DEFAULT_PORT = 8080;

    private static final StaticTypes STATIC_TYPES = new StaticTypes(
            new StaticType("/css", ApplicationContentType.TEXT_CSS),
            new StaticType("/fonts", ApplicationContentType.FONT_TTF),
            new StaticType("/images", ApplicationContentType.IMAGE_PNG),
            new StaticType("/js", ApplicationContentType.TEXT_JAVASCRIPT)
    );

    private static final StaticResolver STATIC_RESOLVER = new StaticResolver(
            "./static",
            STATIC_TYPES,
            ApplicationContentType.TEXT_HTML
    );

    private static final ViewResolver VIEW_RESOLVER = new ViewResolver(
            "./templates",
            ApplicationContentType.TEXT_HTML
    );

    private static final Handlers HANDLERS = new Handlers(
            new RootController(VIEW_RESOLVER),
            new UserController(VIEW_RESOLVER),
            new ResourceHandler(STATIC_RESOLVER, VIEW_RESOLVER)
    );

    public static void main(String[] args) {
        DataBase.addUser(new User("admin", "admin", "admin", "admin@localhost"));
        new WebServer(HANDLERS).listen(DEFAULT_PORT);
    }
}
