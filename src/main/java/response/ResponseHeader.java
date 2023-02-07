package response;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHeader {

    public static void response302Header(DataOutputStream dos, String location) {
        try {
            /*
                HTTP/1.1 302 Found
                Location: http://www.iana.org/domains/example/
             */
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + type + ";charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
