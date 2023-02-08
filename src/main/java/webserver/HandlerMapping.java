package webserver;

import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HandlerMapping {
    private final Map<String, HttpServlet> handlerMap;
    private final AntPathMatcher matcher;

    public HandlerMapping(){
        this.handlerMap = new HashMap<>();
        this.matcher = new AntPathMatcher();
    }

    public void add(String pattern, HttpServlet handler){
        handlerMap.put(pattern, handler);
    }

    public Optional<HttpServlet> getHandler(String url){
        List<HttpServlet> handlers = getHandlers(url);
        if(handlers.size() > 1){
            new RuntimeException("More than one handler registered for the same pattern.");
        }

        return handlers
                .stream()
                .findFirst();
    }

    private List<HttpServlet> getHandlers(String url){
        return handlerMap.keySet().stream()
                .filter(pattern -> matcher.match(pattern, url))
                .map(handlerMap::get)
                .collect(Collectors.toList());
    }
}
