package db;

import static org.assertj.core.api.Assertions.assertThat;

import auth.AuthUserDetails;
import app.user.User;
import app.user.db.UserDatabaseImpl;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDatabaseImplTest {

    UserDatabaseImpl userDatabaseImpl = new UserDatabaseImpl();

    @BeforeEach
    void setUp() {
        userDatabaseImpl.deleteAll();
    }

    @Test
    void insert() {
        // given
        User user = User.builder()
                .userId("1")
                .email("1@1")
                .password("2")
                .name("3")
                .build();

        // when
        User savedUser = (User) userDatabaseImpl.save(user);

        // then
        assertThat(savedUser).isEqualTo(user);

    }

    @Test
    void findByUserId() {
        // given
        User user = User.builder()
                .userId("1")
                .email("1@1")
                .password("2")
                .name("3")
                .build();

        // when
        User savedUser = (User) userDatabaseImpl.save(user);
        User foundUser = (User) userDatabaseImpl.findById(user.getUserId()).get();

        // then
        assertThat(foundUser).isEqualTo(savedUser);

    }


    @Test
    void findAll() {
        // given
        User user = User.builder()
                .userId("1")
                .email("1@1")
                .password("2")
                .name("3")
                .build();

        User user2 = User.builder()
                .userId("2")
                .email("1@1")
                .password("2")
                .name("3")
                .build();
        userDatabaseImpl.save(user);
        userDatabaseImpl.save(user2);

        // when
        Collection<AuthUserDetails> users = userDatabaseImpl.findAll();

        // then
        assertThat(users.size()).isEqualTo(2);
    }

}