package auth.controller;

import static auth.controller.AuthLoginControllerConstants.INDEX_HTML_URL;
import static auth.controller.AuthLoginControllerConstants.LOGIN_FAIL_HTML_URL;
import static infra.utils.response.Response302HeaderUtils.make302ResponseHeader;
import static infra.utils.response.Response302HeaderUtils.make302ResponseWithCookie;

import auth.AuthUserDetailsWithUuid;
import auth.dto.AuthLoginUserDto;
import auth.service.AuthLoginService;
import infra.controller.MyController;
import infra.controller.MyPostMapping;
import infra.utils.request.MyCookie;
import infra.utils.request.MyRequest;
import java.io.DataOutputStream;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthLoginController implements MyController {

    private static final Logger logger = LoggerFactory.getLogger(AuthLoginController.class);
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private final AuthLoginService authLoginService;

    public AuthLoginController(AuthLoginService authLoginService) {
        this.authLoginService = authLoginService;
    }

    @MyPostMapping(paths="/user/login")
    public void login(MyRequest myRequest, DataOutputStream dataOutputStream) {
        Optional<AuthUserDetailsWithUuid> loginUserDetails = authLoginService.login(createAuthLoginUserDto(myRequest));
        if (loginUserDetails.isPresent()) {
            MyCookie cookie = new MyCookie(loginUserDetails.get().getUuid());
            make302ResponseWithCookie(dataOutputStream, INDEX_HTML_URL.url(), cookie,logger);
            return;
        }
        make302ResponseHeader(dataOutputStream, LOGIN_FAIL_HTML_URL.url(), logger);
    }

    private AuthLoginUserDto createAuthLoginUserDto(MyRequest myRequest) {
        return new AuthLoginUserDto(myRequest.getParam(USER_ID), myRequest.getParam(PASSWORD));
    }

}
