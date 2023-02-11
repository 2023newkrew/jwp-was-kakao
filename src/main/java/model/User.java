package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class User {
    private final Long id;
    private final String userId;
    private final String password;
    private final String name;
    private final String email;


    public boolean isMatchPassword(String password) {
        return this.password.equals(password);
    }
}
