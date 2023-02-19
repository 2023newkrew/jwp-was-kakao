package model;

import lombok.Getter;

@Getter
public enum StaticFile {
    CSS("css"),
    JS("js"),
    IMAGES("images"),
    FONTS("fonts"),
    FAVICON("favicon"),
    ;

    private final String value;

    StaticFile(String value) {
        this.value = value;
    }
}
