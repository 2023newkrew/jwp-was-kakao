package app.home.controller;

import static app.home.controller.HomeControllerConstants.HELLO_WORLD_BYTES;
import static infra.utils.response.Response200HeaderUtils.response200Header;
import static infra.utils.response.ResponseBodyUtils.responseBody;
import static infra.utils.response.ResponseUtils.make200TemplatesResponse;

import infra.controller.MyController;
import infra.controller.MyGetMapping;
import infra.utils.request.MyRequest;
import java.io.DataOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public class HomeController implements MyController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @MyGetMapping(paths = "/")
    public void helloWorld(MyRequest myRequest, DataOutputStream dataOutputStream) {
        byte[] body = HELLO_WORLD_BYTES;
        response200Header(dataOutputStream, body.length, logger);
        responseBody(dataOutputStream, body);
    }

    @MyGetMapping(paths = "/index.html")
    public void index(MyRequest request, DataOutputStream dataOutputStream) {
        make200TemplatesResponse(request.getPath(), request.getHeader(HttpHeaders.ACCEPT), dataOutputStream, logger);
    }

}
