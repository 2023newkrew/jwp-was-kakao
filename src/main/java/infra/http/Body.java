package infra.http;

public interface Body {
    public int length();
    public byte[] flat();
    public String toString();
}
