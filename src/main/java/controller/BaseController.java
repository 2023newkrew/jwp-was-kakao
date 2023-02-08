package controller;

public class BaseController {

    private static BaseController instance = new BaseController();

    private BaseController() {
    }

    public static BaseController getInstance() {
        return instance;
    }

    public String hello() {
        return "Hello world";
    }
}
