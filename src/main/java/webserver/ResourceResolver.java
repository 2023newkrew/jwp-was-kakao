package webserver;

import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceResolver {
    private final Map<String, String> resourceMap;
    private final AntPathMatcher matcher;

    public ResourceResolver(){
        this.resourceMap = new HashMap<>();
        this.matcher = new AntPathMatcher();
    }

    public void addResourceHandler(String pattern, String location){
        resourceMap.put(pattern, location);
    }

    public String resolve(String pattern){
        if(getLocations(pattern).size() > 1){
            new RuntimeException("More than one location registered for the same pattern.");
        }
        return resourceMap.get(pattern);
    }

    public boolean isResolvable(String pattern){
        return getLocations(pattern)
                .size() == 1L;
    }

    private List<String> getLocations(String targetPattern){
        return resourceMap.keySet().stream()
                .filter(pattern -> matcher.match(pattern, targetPattern))
                .collect(Collectors.toList());
    }
}
