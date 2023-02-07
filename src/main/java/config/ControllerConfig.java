package config;


import controller.HomeController;
import controller.IcoController;
import controller.MyController;
import controller.StaticController;
import dispatcherservlet.FrontController;
import java.util.List;

public enum ControllerConfig {
    INSTANCE;
    private static final List<MyController> controllers = List.of(
            new HomeController(),
            UserConfig.INSTANCE.getUserController(),
            new StaticController(),
            new IcoController()
    );

    private static final FrontController frontController = new FrontController(controllers);

    public FrontController getFrontController(){
        return frontController;
    }

}
