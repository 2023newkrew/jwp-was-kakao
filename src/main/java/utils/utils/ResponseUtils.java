package utils.utils;

import lombok.experimental.UtilityClass;
import model.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

@UtilityClass
public class ResponseUtils {
    private static final int DEFAULT_OFFSET = 0;

    public void doResponse(DataOutputStream dos, HttpResponse response) throws IOException {
        dos.writeBytes(response.getStatusLine());
        writeResponseHeader(dos, response);
        dos.writeBytes("\r\n");

        if (response.isBodyExists()) {
            dos.write(response.getBody(), DEFAULT_OFFSET, response.getBodyLength());
        }

        dos.flush();
    }

    private void writeResponseHeader(DataOutputStream dos, HttpResponse response) throws IOException {
        for (Map.Entry<String, String> entry : response.getHeaderEntrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + " \r\n");
        }
    }
}
