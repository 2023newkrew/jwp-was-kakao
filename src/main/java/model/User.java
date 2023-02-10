package model;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class User {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    public Map<String, String> toMap() {
        return Stream.of(
                new AbstractMap.SimpleEntry<>("userId", userId),
                new AbstractMap.SimpleEntry<>("password", password),
                new AbstractMap.SimpleEntry<>("name", name),
                new AbstractMap.SimpleEntry<>("email", email)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
