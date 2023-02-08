package controller;

import model.dto.MyHeaders;
import model.dto.MyParams;

import java.io.DataOutputStream;

public interface MyController {

    boolean canHandle(MyHeaders headers, MyParams params);

    void handle(MyHeaders headers, MyParams params, DataOutputStream dataOutputStream);
}
