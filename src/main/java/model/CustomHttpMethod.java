package model;

import java.util.Arrays;

public enum CustomHttpMethod {

    GET, POST;

    public static CustomHttpMethod from(String method) throws IllegalStateException {
        return Arrays.stream(CustomHttpMethod.values())
                .filter(customHttpMethod -> customHttpMethod.name().equalsIgnoreCase(method))
                .findAny().orElseThrow(() -> new IllegalStateException("해당하는 HTTP 메서드를 찾을 수 없습니다."));
    }

}
