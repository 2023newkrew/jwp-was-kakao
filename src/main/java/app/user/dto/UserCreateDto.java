package app.user.dto;

import static infra.utils.validator.Validator.checkField;

import app.user.User;

public class UserCreateDto {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    protected UserCreateDto(String userId, String password, String name, String email) {
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

    public User toEntity(){
        checkFields();
        return User.builder()
                .userId(userId)
                .email(email)
                .name(name)
                .password(password)
                .build();
    }

    public static UserCreateDtoBuilder builder(){
        return new UserCreateDtoBuilder();
    }

    public static class UserCreateDtoBuilder{

        private String userId;
        private String password;
        private String name;
        private String email;
        public UserCreateDtoBuilder userId(String userId){
            this.userId=userId;
            return this;
        }

        public UserCreateDtoBuilder password(String password){
            this.password=password;
            return this;
        }
        public UserCreateDtoBuilder name(String name){
            this.name=name;
            return this;
        }
        public UserCreateDtoBuilder email(String email){
            this.email=email;
            return this;
        }
        public UserCreateDto build(){
            return new UserCreateDto(userId, password, name, email);
        }

    }
    private void checkFields() {
        checkField(userId, "userId");
        checkField(email, "email");
        checkField(password, "password");
        checkField(name, "name");
    }
}
