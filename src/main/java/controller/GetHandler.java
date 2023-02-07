package controller;

import request.RequestParams;
import utils.FileIoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class GetHandler {
    private final BufferedReader br;

    public GetHandler(BufferedReader br) {
        this.br = br;
    }

    public String getRequestUrl(RequestParams request, String s) throws IOException {
        if (request.getUrl().startsWith("/css") || request.getUrl().startsWith("/js")){
            return "./static" + request.getUrl();
        }
        if (request.getUrl().equals("/")){
            return null;
        }
        if (request.getUrl().startsWith("/") || request.getUrl().endsWith("html")){
            return "./templates" + request.getUrl();
        }
        while (!"".equals(s)) {
            s = br.readLine();
        }
        return request.getUrl();
    }

    public byte[] getBody(String requestUrl) throws IOException, URISyntaxException {
        try {
            return FileIoUtils.loadFileFromClasspath(requestUrl);
        } catch (NullPointerException e) {
            return "Hello world".getBytes();
        }
    }
}