package logics;

import logics.get.DefaultResponseController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UrlMatcher {
    private final Map<String, Controller> matcher = new ConcurrentHashMap<>();
    private final Controller defaultController = new DefaultResponseController();

    public UrlMatcher(){}

    public UrlMatcher addMatch(String url, Controller controller){
        matcher.put(url, controller);
        return this;
    }

    public Controller getMatchingUrl(String url){
        return matcher.get(url);
    }

    public Controller getDefaultController() {
        return defaultController;
    }
}
