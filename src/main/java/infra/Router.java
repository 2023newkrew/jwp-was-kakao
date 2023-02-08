package infra;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private Controller viewController;
    private Map<String, Controller> serviceControllers;

    public Router(Controller viewController) {
        this.viewController = viewController;
        this.serviceControllers = new HashMap<>();
    }

    public void set(String uri, Controller controller) {
        serviceControllers.put(uri, controller);
    }

    public Controller controller(String uri) {
        for (Map.Entry<String, Controller> entry : serviceControllers.entrySet()) {
            if (uri.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return viewController;
    }
}
