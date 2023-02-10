package controller;

import webserver.request.HttpRequest;
import webserver.request.MyHeaders;
import webserver.request.MyParams;

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

    public MyController handlerMapping(HttpRequest httpRequest, DataOutputStream dataOutputStream){
        return controllers.stream()
                .filter((con) -> con.canHandle(httpRequest))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("매핑된 컨트롤러가 존재하지 않습니다."));
    }
}
