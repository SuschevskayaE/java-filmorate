package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("select * from users", UserDbStorage::createUser);
    }

    @Override
    public User get(long id) {
        try {
            return jdbcTemplate.queryForObject("select * from users where id = ?", UserDbStorage::createUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException(String.format("В таблице нет одной записи с id = %s", id));
        }
    }

    @Override
    public User create(User data) {
        final User newData = validate(data);

        String sqlQuery = "INSERT INTO users(email, login, name, birthday) " +
                "VALUES(?, ?, ?,?)";

        KeyHolder kayHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, newData.getEmail());
            stmt.setString(2, newData.getLogin());
            stmt.setString(3, newData.getName());
            stmt.setTimestamp(4, Timestamp.valueOf(newData.getBirthday().atStartOfDay()));
            return stmt;
        }, kayHolder);
        newData.setId(kayHolder.getKey().longValue());
        log.debug("Добавлен новый элемент':" + newData);

        return newData;
    }

    @Override
    public User update(User data) {
        if (data.getId() == null) {
            throw new DataNotFoundException(String.format("Элемент %s не найден", data));
        }
        long id = get(data.getId()).getId();
        data = validate(data);

        String sqlQuery = "UPDATE users " +
                "SET " +
                "email =?, " +
                "Login =?, " +
                "name =?, " +
                "birthday = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery,
                data.getEmail(),
                data.getLogin(),
                data.getName(),
                data.getBirthday(),
                id);
        log.debug("Элемент обновлен:" + data);
        return data;
    }

    @Override
    public User validate(User data) {
        if (data.getName().isBlank()) {
            data.setName(data.getLogin());
            log.debug("Заменено имя пользователя на логин: " + data.getName());
        }
        return data;
    }

    static User createUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .build();
    }
}
