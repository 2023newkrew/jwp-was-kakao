package utils;

import webserver.Headers;
import webserver.RequestParameters;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {
    public static Headers parse(BufferedReader br) {
        Headers headers = new Headers();
        parseRequestLine(headers, br);
        parseHeader(headers, br);
        return headers;
    }

    private static void parseRequestLine(Headers headers, BufferedReader br) {
        try {
            String line = br.readLine();
            if (line == null) return;
            String[] splitLine = line.split(" ");
            headers.put("Method", splitLine[0]);
            headers.put("URL", splitLine[1]);
            headers.put("Protocol", splitLine[2]);
        } catch (IOException e) {
            return;
        }
    }

    private static void parseHeader(Headers headers, BufferedReader br) {
        try{
            String line = br.readLine();
            while (!"".equals(line)) {
                String[] splitLine = line.split(": ");
                headers.put(splitLine[0], splitLine[1]);
                line = br.readLine();
                if (line == null) return;
            }
        } catch (IOException e) {
            return;
        }
    }

    public static RequestParameters parse(String query) {
        RequestParameters requestParameters = new RequestParameters();
        String[] params = query.split("&");
        for (String param : params) {
            String[] splitParam = param.split("=");
            if (splitParam.length != 2) {
                return null;
            }
            requestParameters.put(splitParam[0], splitParam[1]);
        }
        return requestParameters;
    }
}
