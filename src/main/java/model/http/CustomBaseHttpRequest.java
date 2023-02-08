package model.http;

public abstract class CustomBaseHttpRequest {

    public abstract String get(String key);
    public abstract void put(String key, String value);

}
