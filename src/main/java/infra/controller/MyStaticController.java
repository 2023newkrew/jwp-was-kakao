package infra.controller;

import infra.utils.request.MyRequest;
import java.io.DataOutputStream;

public interface MyStaticController {
    void handle(MyRequest myRequest, DataOutputStream dataOutputStream);
}
