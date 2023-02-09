package controller;

import model.dto.MyHeaders;
import model.dto.MyParams;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet {
    List<MyController> controllers;
    public DispatcherServlet(){
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
                .orElseThrow(() -> new IllegalArgumentException("매핑된 컨트롤러가 존재하지 않습니다."))
                .handle(headers, params, dataOutputStream);
    }

    public boolean canHandle(MyHeaders headers, MyParams params){
        return controllers.stream()
                .anyMatch(con -> con.canHandle(headers, params));
    }
}
