package model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserListViewDto {
    private Long id;
    private String userId;
    private String name;
    private String email;
}
