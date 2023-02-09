package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;
import webserver.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static Map<String, String> getParameters(final String queryString, final HttpResponse response) {
        List<String> queryParams = List.of(queryString.split("&"));
        Map<String, String> parameters = new HashMap<>();
        queryParams.forEach((queryParam) -> {
            String[] queryParamSplit = queryParam.split("=");
            if (queryParamSplit.length == 2) {
                parameters.put(queryParam.split("=")[0], queryParam.split("=")[1]);
            } else {
                logger.error("Parameter가 base64 url-safe encoding이 아니거나 올바른 Query String이 아닙니다.");
                response.setStatus(HttpStatus.BAD_REQUEST);
            }
        });
        return parameters;
    }

    public static byte[] getFileContent(final String uri, final HttpResponse response) {
        try {
            if (uri.endsWith(".html")) {
                return FileIoUtils.loadFileFromClasspath("./templates" + uri.replaceFirst("^\\.+", ""));
            } else {
                return FileIoUtils.loadFileFromClasspath("./static" + uri.replaceFirst("^\\.+", ""));
            }
        } catch (Exception e) {
            logger.error("올바른 path가 아닙니다.");
            response.setStatus(HttpStatus.NOT_FOUND);
        }
        return "".getBytes();
    }

    public static HttpRequest parseRequestMessage(final BufferedReader reader, final HttpResponse response) {
        String startLine = readStartLine(reader);
        List<String> Headers = readHeaders(reader);

        String uri = getURI(startLine, response).orElse("/");
        HttpMethod method = getMethod(startLine, response);
        HttpHeaders headers = getHttpHeaders(Headers, response);
        HttpCookies cookie = getCookies(headers, response);
        String[] pathAndParameters = uri.split("\\?");
        String path = pathAndParameters[0];

        Map<String, String> parameters = new HashMap<>();

        if (method.equals(HttpMethod.GET)) {
            if (pathAndParameters.length == 2) {
                parameters = getParameters(pathAndParameters[1], response);
            }
        } else if (method.equals(HttpMethod.POST)) {
            try {
                String data = IOUtils.readData(reader, Integer.parseInt(headers.getHeader("Content-Length").getValue()));
                System.out.println(data);
                parameters.putAll(getParameters(data, response));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return new HttpRequest(path, method, headers, parameters, cookie);
    }

    private static String readStartLine(final BufferedReader reader) {
        String startLine = null;
        try {
            startLine = reader.readLine();
            System.out.println(startLine);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return startLine;
    }

    private static HttpHeaders getHttpHeaders(final List<String> headerLines, final HttpResponse response) {
        Map<String, HttpHeader> headers = new HashMap<>();
        headerLines.forEach(line -> {
            String[] headerKeyAndValue = line.split(": ");
            if (headerKeyAndValue.length == 2) {
                headers.put(headerKeyAndValue[0], new HttpHeader(headerKeyAndValue[0], headerKeyAndValue[1]));
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST);
            }
        });
        return new HttpHeaders(headers);
    }

    private static List<String> readHeaders(final BufferedReader reader) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            do {
                line = reader.readLine();
                System.out.println(line);
                if (Objects.isNull(line) || line.isEmpty()) break;
                lines.add(line);
            } while (true);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return lines;
    }

    private static HttpMethod getMethod(final String startLine, final HttpResponse response) {
        try {
            return HttpMethod.valueOf(startLine.split(" ")[0]);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return HttpMethod.NONE;
    }

    private static Optional<String> getURI(final String startLine, final HttpResponse response) {
        try {
            return Optional.of(startLine.split(" ")[1]);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return Optional.empty();
    }

    private static HttpCookies getCookies(final HttpHeaders headers, final HttpResponse response) {
        Map<String, HttpCookie> cookies = new ConcurrentHashMap<>();
        HttpHeader cookieStrings = headers.getHeader("Cookie");
        if (Objects.isNull(cookieStrings)) {
            return new HttpCookies(cookies);
        }
        String[] cookieString = cookieStrings.getValue().split("; ");
        for (String s : cookieString) {
            String[] keyAndValue = s.split("=");
            if (keyAndValue.length == 2) {
                cookies.put(keyAndValue[0], new HttpCookie(keyAndValue[0], keyAndValue[1]));
            } else {
                logger.error("잘못된 쿠키 형식입니다.");
                response.setStatus(HttpStatus.BAD_REQUEST);
            }
        }
        return new HttpCookies(cookies);
    }
}