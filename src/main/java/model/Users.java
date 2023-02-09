package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Users {
    private final List<User> users;

    public Users(Collection<User> users) {
        this.users = new ArrayList<>(users);
    }
}
