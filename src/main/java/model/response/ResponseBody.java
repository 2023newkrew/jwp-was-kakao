package model.response;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseBody {
    byte[] body;

    public ResponseBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public boolean isEmpty() {
        return length() == 0;
    }

    public byte[] get() {
        return body;
    }

    public int length() {
        return body.length;
    }

}
