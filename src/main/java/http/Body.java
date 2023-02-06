package http;

public class Body {

    private final byte[] body;

    public Body(byte[] body) {
        this.body = body;
    }

    public Body(String body) {
        this.body = body.getBytes();
    }

    public static Body empty() {
        return new Body("");
    }

    public int length() {
        return body.length;
    }

    public byte[] asByte() {
        return body;
    }

}
