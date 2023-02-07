package model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class UserRequest {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User toEntity(Long id){
        return new User(id, userId, password, name, email);
    }
}
