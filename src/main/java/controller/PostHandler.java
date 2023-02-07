package controller;

import supports.StringParser;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class PostHandler {
    private final BufferedReader br;
    private final StringParser stringParser;

    public PostHandler(BufferedReader br, StringParser stringParser) {
        this.br = br;
        this.stringParser = stringParser;
    }

    public String getRequestUrl(BufferedReader br) {
        return "/index.html";
    }

    public String getRequestBody(String requestInfo) throws IOException {
        while (stringParser.getContentLength(requestInfo) == -1) {
            requestInfo = br.readLine();
        }
        if (requestInfo.length() == 0) {
            return null;
        }
        return IOUtils.readData(br, stringParser.getContentLength(requestInfo));
    }
}
