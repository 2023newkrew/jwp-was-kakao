package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            HeaderInfo headerInfo = getHeaderInfo(bufferedReader);

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = headerInfo.getResponse();

            System.out.println("###################");
            System.out.println(headerInfo);
            response200Header(dos, body.length, headerInfo.getAccept());
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HeaderInfo getHeaderInfo(BufferedReader bufferedReader) throws IOException {
        HeaderInfo headerInfo = new HeaderInfo(bufferedReader.readLine());
        String line = bufferedReader.readLine();
        while (!"".equals(line) && Objects.nonNull(line)) {
            System.out.println(line);
            headerInfo.readNextLine(line);
            line = bufferedReader.readLine();
        }
        return headerInfo;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + type + ";charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

enum Directory {
    CSS, FONTS, IMAGES, JS;
    private static final Map<String, Directory> mappings = new HashMap<>(16);

    static {
        for (Directory directory : values()) {
            mappings.put(directory.name(), directory);
        }
    }

    @Nullable
    public static Directory resolve(@Nullable String dir) {
        return (dir != null ? mappings.get(dir) : null);
    }
}

class HeaderInfo {
    private HttpMethod method;
    private final String path;
    private String root;
    private final Map<String, String> mappings = new HashMap<>();

    private String accept;

    public HeaderInfo(String firstLine) {
        if (Objects.isNull(firstLine)) {
            throw new RuntimeException("kk");
        }
        String[] tokens = firstLine.split(" ");
        HttpMethod method = HttpMethod.resolve(tokens[0]);
        if (Objects.isNull(method)) {
            throw new RuntimeException("zz");
        }
        this.method = method;
        this.path = setPath(tokens[1]);
    }

    public void readNextLine(String line) {
        String[] tokens = line.split(": ");
        if (tokens[0].equals("Accept")) {
            setAccept(tokens[1]);
            return;
        }
        mappings.put(tokens[0], tokens[1]);
    }

    public byte[] getResponse() {
        try {
            return FileIoUtils.loadFileFromClasspath(root + path);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("lol");
        } catch (NullPointerException e) {
            return new byte[]{};
        }
    }

    private void setAccept(String value) {
        //TODO exception handling
        accept = value.split(",")[0];
    }

    private String setPath(String path) {
        String[] tokens = path.split("/");
        if (tokens.length < 2) {
            return path;
        }
        Directory directory = Directory.resolve(tokens[1].toUpperCase());
        if (Objects.isNull(directory)) {
            root = "./templates";
            return path;
        }
        root = "./static";
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getRoot() {
        return root;
    }

    public Map<String, String> getMappings() {
        return mappings;
    }

    public String getAccept() {
        return accept;
    }

    @Override
    public String toString() {
        return "HeaderInfo{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", root='" + root + '\'' +
                ", mappings=" + mappings +
                ", accept='" + accept + '\'' +
                '}';
    }
}