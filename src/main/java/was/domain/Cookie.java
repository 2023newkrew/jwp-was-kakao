package was.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.User;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Cookie {
    private final UUID uuid;
    private final String path;
    private final User user;
}
