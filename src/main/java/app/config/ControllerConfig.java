package app.config;

import infra.controller.MyController;
import infra.controller.MyStaticController;
import infra.dispatcherservlet.FrontController;
import infra.filter.MyFilter;
import java.util.List;

public class ControllerConfig {
    FrontController frontController;

    public ControllerConfig(MyStaticController myStaticController, List<MyFilter> filters, MyController... controllers) {
        this.frontController = new FrontController(myStaticController, filters,controllers);
    }

    public FrontController getFrontController() {
        return frontController;
    }
}
