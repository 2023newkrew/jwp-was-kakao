package logics.controller.support;

import logics.controller.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UrlMatcher helps controller associate specified-url with other sub-controllers.
 */
public class UrlMatcher {
    private final Map<String, Controller> matcher = new ConcurrentHashMap<>();

    public UrlMatcher(){}

    public void addMatch(String url, Controller controller){
        matcher.put(url, controller);
    }

    public Controller getMatchingUrl(String url){
        return matcher.get(url);
    }
}
