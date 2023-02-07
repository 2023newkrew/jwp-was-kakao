package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest httpRequest = getRequest(br);
            HttpResponse httpResponse = new RequestController().requestMapping(httpRequest);

            sendResponse(dos, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpRequest getRequest(BufferedReader br) throws IOException {
        // start line
        String line = br.readLine();
        String[] lineParts = line.split(" ");

        HttpMethod httpMethod = HttpMethod.valueOf(lineParts[0]);
        String uri = lineParts[1];
        String httpVersion = lineParts[2];

        // headers
        Map<String, String> headers = new HashMap<>();
        while ((line = br.readLine()) != null && line.length() > 0) {
            int headerSeparatorIndex = line.indexOf(": ");
            String headerName = line.substring(0, headerSeparatorIndex);
            String headerValue = line.substring(headerSeparatorIndex + ": ".length());
            headers.put(headerName, headerValue);
        }

        // body
        String body = "";
        if (headers.containsKey("Content-Length")) {
            body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
        }

        return new HttpRequest(httpMethod, uri, httpVersion, headers, body);
    }

    private void sendResponse(DataOutputStream dos, HttpResponse httpResponse){
        try {
            // status line
            dos.writeBytes(httpResponse.version + " " + httpResponse.status + " \r\n");
            // header
            for (String line: httpResponse.headers) {
                dos.writeBytes(line);
            }
            // body
            dos.write(httpResponse.body, 0, httpResponse.body.length);
            // flush
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
