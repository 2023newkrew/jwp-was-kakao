package filter;

import http.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FilterManager {
    private static final Logger logger = LoggerFactory.getLogger(FilterManager.class);
    private static final Map<String, Filter> filters;

    private FilterManager() {
    }

    private static class FilterManagerHolder {
        private static final FilterManager instance = new FilterManager();
    }

    public static FilterManager getInstance() {
        return FilterManagerHolder.instance;
    }

    static {
        filters = Map.of(
                "/user/login.html", UnauthorizedFilter.getInstance(),
                "/user/login_failed.html", UnauthorizedFilter.getInstance(),
                "/user/list.html", AuthorizedFilter.getInstance()
        );
    }

    public void mapFilter(Request request) {
        String uriPath = request.getStartLine().getUri().getPath();
        if (filters.containsKey(uriPath)) {
            Filter filter = filters.get(uriPath);
            filter.doFilter(request);
        }
    }
}
