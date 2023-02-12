package webserver.filter;

import java.util.ArrayList;
import java.util.List;

public class ApplicationFilterConfig implements FilterConfig {
    private final List<Filter> filters = new ArrayList<>();

    public ApplicationFilterConfig() {
        filters.add(new SessionFilter());
        filters.add(new LoginFilter());
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}
