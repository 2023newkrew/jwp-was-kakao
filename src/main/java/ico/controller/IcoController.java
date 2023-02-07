package ico.controller;

import static ico.controller.IcoControllerApis.ICO_API;
import static utils.response.ResponseUtils.make200TemplatesResponse;

import excpetion.NotMatchException;
import infra.controller.BaseMyController;
import java.io.DataOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import utils.request.MyRequest;

public class IcoController extends BaseMyController {

    private static final Logger logger = LoggerFactory.getLogger(IcoController.class);

    public IcoController() {
        super(IcoControllerApis.values());
    }

    @Override
    public void handle(MyRequest myRequest, DataOutputStream dataOutputStream) {
        if (myRequest.getApi().equals(ICO_API.getApi())) {
            getIco(myRequest.getPath(), myRequest.getHeader(HttpHeaders.ACCEPT).split(",")[0], dataOutputStream);
            return;
        }
        throw new NotMatchException("api cannot be match", "api should be matched",
                IcoController.class.getSimpleName());
    }
    private void getIco(String path, String contentType, DataOutputStream dataOutputStream) {
        make200TemplatesResponse(path, contentType, dataOutputStream, logger);
    }
}
