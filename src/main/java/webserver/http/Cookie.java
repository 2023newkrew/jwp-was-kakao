package webserver.http;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import lombok.Getter;

@Getter
public class Cookie {

    private final String name;

    private final String value;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Cookie(String cookie) {
        String[] cookieParts = cookie.split("=");
        this.name = cookieParts[0].trim();
        this.value = cookieParts[1].trim();
    }

    @Override
    public String toString() {
        return StringUtils.join(
                name,
                "=",
                value,
                "; Path=/"
        );
    }
}
