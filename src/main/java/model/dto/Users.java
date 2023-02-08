package model.dto;

import lombok.Getter;
import model.User;

import java.util.List;

@Getter
public class Users {

    private final List<User> users;

    public Users(List<User> users) {
        this.users = users;
    }
}
