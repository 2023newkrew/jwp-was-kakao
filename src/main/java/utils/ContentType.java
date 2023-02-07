package utils;

public enum ContentType {
    HTML("text/html;charset=utf-8 "),
    CSS("text/css;charset=utf-8 "),
    JS("application/javascript;charset=utf-8 "),
    ICO("image/x-icon "),
    ;

    private final String type;
    ContentType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public static ContentType setContentType(String s){
        if (s.startsWith("/css")) return CSS;
        if (s.startsWith("/js")) return JS;
        if (s.endsWith("ico")) return ICO;
        return HTML;
    }
}
