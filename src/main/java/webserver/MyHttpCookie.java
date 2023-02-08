package webserver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MyHttpCookie {

    private final String name;
    private final String value;
    private int maxAge;
    private String path;

    @Override
    public String toString() {
        StringBuilder header = new StringBuilder();
        header.append(this.name);
        header.append('=');

        if (this.value != null && this.value.length() > 0) {
            header.append(this.value);
        }

        if (this.path != null && this.path.length() > 0) {
            header.append("; Path=");
            header.append(this.path);
        }

        return header.toString();
    }
}
