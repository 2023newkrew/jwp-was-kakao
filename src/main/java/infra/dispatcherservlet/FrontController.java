package infra.dispatcherservlet;


import static infra.utils.response.ResponseErrorHeaderUtils.makeErrorHeader;

import infra.controller.MyController;
import infra.controller.MyStaticController;
import infra.exception.NotMatchException;
import infra.filter.MyFilter;
import infra.utils.request.MyRequest;
import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontController {

    private final HandlerAdaptor handlerAdaptor;
    private final MyStaticController myStaticController;
    private final Logger logger = LoggerFactory.getLogger(FrontController.class);
    private final List<MyFilter> filters;

    public FrontController(MyStaticController myStaticController, List<MyFilter> filters,MyController... controllers) {
        this.myStaticController = myStaticController;
        this.handlerAdaptor = new HandlerAdaptor(controllers);
        this.filters = filters;
    }
    public void handle(MyRequest myRequest, DataOutputStream dataOutputStream)
            throws InvocationTargetException, IllegalAccessException {
        doFilter(myRequest);
        boolean handled = handlerAdaptor.handle(myRequest, dataOutputStream);
        if(!handled){
            handleStatic(myRequest, dataOutputStream);
        }
    }

    private void doFilter(MyRequest myRequest) {
        filters.forEach(filter -> filter.accept(myRequest));
    }

    private void handleStatic(MyRequest myRequest, DataOutputStream dataOutputStream) {
        try {
            myStaticController.handle(myRequest, dataOutputStream);
        }catch(NotMatchException e){
            makeErrorHeader(dataOutputStream, logger, e);
            logger.error(e.getMessage());
        }
    }

}
