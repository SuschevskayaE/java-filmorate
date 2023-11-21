package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void getAllUsersTrue() {
        User newUser = User.builder()
                .email("email@y.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1999, 10, 2))
                .build();

        User newUser2 = User.builder()
                .email("email2@y.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, 10, 2))
                .build();

        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        long id1 = userStorage.create(newUser).getId();
        long id2 = userStorage.create(newUser2).getId();

        List<User> savedUsers = userStorage.getAll();

        assertThat(savedUsers.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
        assertThat(savedUsers.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser2);
    }

    @Test
    public void createUserAndGetUserByIdTrue() {
        User newUser = User.builder()
                .email("email3@y.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1999, 10, 2))
                .build();

        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        long id = userStorage.create(newUser).getId();

        User savedUser = userStorage.get(id);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void updateUserTrue() {
        User newUser = User.builder()
                .email("email3@y.ru")
                .login("login1")
                .name("login1")
                .birthday(LocalDate.of(1999, 10, 2))
                .build();

        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        long id = userStorage.create(newUser).getId();


        newUser.setId(id);
        newUser.setEmail("email2@y.ru");
        newUser.setLogin("login4");
        newUser.setName("login4");
        newUser.setBirthday(LocalDate.of(1822, 10, 2));

        userStorage.update(newUser);

        User savedUser = userStorage.get(id);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }
}
