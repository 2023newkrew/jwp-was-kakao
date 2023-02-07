package http;

public enum ResponseStatus {

    OK(200),
    FOUND(302);

    private final int value;

    ResponseStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value + " " + name();
    }
}
