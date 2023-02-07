package webserver;

public class Application {
    public static void main(String[] args) throws Exception{
        int port = 8080;
        if (!(args == null || args.length == 0)) {
            port = Integer.parseInt(args[0]);
        }

        WebServer webServer = new WebServer(port);
        webServer.start();
    }
}
