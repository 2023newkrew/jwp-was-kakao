package webserver.handler.response.header;

import java.io.DataOutputStream;

public interface ResponseHeader {
    void generate(DataOutputStream dataOutputStream, int lengthOfBodyContent, String accept);
}
