package controller;

import model.dto.MyHeaders;
import model.dto.MyParams;
import webserver.response.ResponseEntity;

import java.io.DataOutputStream;

public interface MyController {

    boolean canHandle(MyHeaders headers, MyParams params);

    ResponseEntity handle(MyHeaders headers, MyParams params, DataOutputStream dataOutputStream);
}
