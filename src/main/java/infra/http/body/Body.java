package infra.http.body;

public interface Body {
    public int length();
    public byte[] flat();
    public String toString();
}
