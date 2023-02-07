package model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
public class UserListViewDto {
    private Long id;
    private String userId;
    private String name;
    private String email;

}
