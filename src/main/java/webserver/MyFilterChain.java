package webserver;

import java.util.Iterator;
import java.util.List;
import model.MyHttpRequest;
import model.MyHttpResponse;
import webserver.filter.SessionFilter;

public class MyFilterChain {

    private final List<MyFilter> filters = List.of(SessionFilter.getInstance());
    private final Iterator<MyFilter> iterators = filters.iterator();

    public void doChain(MyHttpRequest httpRequest, MyHttpResponse httpResponse) {
        if (iterators.hasNext()) {
            iterators.next().doChain(httpRequest, httpResponse, this);
        }
    }
}
