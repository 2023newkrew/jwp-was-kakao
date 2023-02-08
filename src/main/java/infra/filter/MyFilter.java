package infra.filter;

import infra.utils.request.MyRequest;

public interface MyFilter {
    void accept(MyRequest myRequest);
}
