package controller;

import utils.MyHeaders;
import utils.MyParams;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FrontController {
    List<MyController> controllers;
    public FrontController(){
        controllers = new ArrayList<>();
    }

    public void addController(MyController myController){
        controllers.add(myController);
    }

    public void addAll(MyController... myControllers){
        controllers.addAll(List.of(myControllers));
    }

    public void handlerMapping(MyHeaders headers, MyParams params, DataOutputStream dataOutputStream){
        controllers.stream()
                .filter((con) -> con.canHandle(headers, params))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 메소드 입니다."))
                .handle(headers, params, dataOutputStream);
    }

    public boolean canHandle(MyHeaders headers, MyParams params){
        return controllers.stream()
                .anyMatch(con -> con.canHandle(headers, params));
    }
}
