package controller;

import response.Response;

import java.util.Map;

public interface Controller {

    Response handleRequest(String uri, Map<String, String> params);
}
