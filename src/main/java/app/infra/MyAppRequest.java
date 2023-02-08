package app.infra;

import auth.AuthUserDetails;
import app.user.vo.LoginUser;
import infra.utils.request.MyRequest;
import java.util.Objects;

public class MyAppRequest {

    private final MyRequest myRequest;

    private MyAppRequest(MyRequest myRequest) {
        this.myRequest = myRequest;
    }

    public static MyAppRequest of(MyRequest myRequest) {
        return new MyAppRequest(myRequest);
    }

    public LoginUser getLoginUser() {
        AuthUserDetails authUserDetails = (AuthUserDetails) myRequest.getContext(AuthUserDetails.class);
        if(Objects.nonNull(authUserDetails)){
            return new LoginUser(authUserDetails);
        }
        return null;
    }

    public MyRequest getMyRequest() {
        return myRequest;
    }
}
