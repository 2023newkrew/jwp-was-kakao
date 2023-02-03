package webserver;

import constant.HttpMethod;

public class ViewController extends ApiController {
    private static final ViewController instance;
    private ViewController() {}

    static {
        instance = new ViewController();
    }

    public static ViewController getInstance() {
        return instance;
    }

}
