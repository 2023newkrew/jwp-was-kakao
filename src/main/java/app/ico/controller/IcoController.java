package app.ico.controller;

import static infra.utils.response.ResponseUtils.make200TemplatesResponse;

import infra.controller.MyController;
import infra.controller.MyGetMapping;
import infra.utils.request.MyRequest;
import java.io.DataOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public class IcoController implements MyController {

    private final Logger logger = LoggerFactory.getLogger(IcoController.class);
    @MyGetMapping(paths = "/favicon.ico")
    public void ico(MyRequest myRequest, DataOutputStream dataOutputStream) {
        make200TemplatesResponse(myRequest.getPath(), myRequest.getHeader(HttpHeaders.ACCEPT), dataOutputStream, logger);
    }
}
