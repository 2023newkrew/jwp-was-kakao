package app;

import app.config.AppConfig;
import infra.webserver.WebServer;

public class Application {
    public static void main(String[] args) throws Exception {
        WebServer.run(AppConfig.getInstance().getControllerConfig().getFrontController(), args);
    }
}
