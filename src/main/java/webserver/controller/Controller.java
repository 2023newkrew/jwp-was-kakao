package webserver.controller;

import webserver.parser.Response;

import java.util.Map;

public interface Controller {

    Response handleRequest(String uri, Map<String, String> params);
}
