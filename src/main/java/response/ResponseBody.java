package response;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseBody {

    public static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
