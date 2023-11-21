package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.UsersRelationStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UsersRelationDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private long oncUserId;
    private long twoUserId;

    @BeforeEach
    public void setUp() {
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
        oncUserId = userStorage.create(newUser).getId();
        twoUserId = userStorage.create(newUser2).getId();
    }

    @Test
    public void addFriendTrue() {
        UsersRelationStorage usersRelationStorage = new UsersRelationDbStorage(jdbcTemplate);
        usersRelationStorage.addFriend(oncUserId, twoUserId);

        String sqlQuery = "SELECT * " +
                "FROM users_relations " +
                "WHERE primary_user_id = ? " +
                "AND  secondary_user_id = ?";

        List<Long> friends = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                        rs.getLong("id"),
                oncUserId, twoUserId);
        assertThat(friends)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    public void removeFriendTrue() {
        UsersRelationStorage usersRelationStorage = new UsersRelationDbStorage(jdbcTemplate);
        usersRelationStorage.addFriend(oncUserId, twoUserId);
        usersRelationStorage.removeFriend(oncUserId, twoUserId);

        String sqlQuery = "SELECT * " +
                "FROM users_relations " +
                "WHERE primary_user_id = ? " +
                "AND  secondary_user_id = ?";

        List<Long> friends = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                        rs.getLong("id"),
                oncUserId, twoUserId);
        assertThat(friends)
                .isNullOrEmpty();
    }

    @Test
    public void getFriendsTrue() {
        UsersRelationStorage usersRelationStorage = new UsersRelationDbStorage(jdbcTemplate);
        usersRelationStorage.addFriend(oncUserId, twoUserId);

        List<Long> friends = usersRelationStorage.getFriends(oncUserId);

        assertThat(friends)
                .isNotNull()
                .containsOnlyOnce(twoUserId);
    }

    @Test
    public void getCommonFriendsTrue() {
        User newUser = User.builder()
                .email("email3@y.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1999, 10, 2))
                .build();

        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        long threeUserId = userStorage.create(newUser).getId();

        UsersRelationStorage usersRelationStorage = new UsersRelationDbStorage(jdbcTemplate);
        usersRelationStorage.addFriend(oncUserId, twoUserId);
        usersRelationStorage.addFriend(threeUserId, twoUserId);
        usersRelationStorage.addFriend(threeUserId, oncUserId);

        List<Long> friends = usersRelationStorage.getCommonFriends(oncUserId, threeUserId);

        assertThat(friends)
                .isNotNull()
                .containsOnlyOnce(twoUserId);
    }
}
