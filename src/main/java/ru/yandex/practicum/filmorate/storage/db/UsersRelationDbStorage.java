package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.storage.UsersRelationStorage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsersRelationDbStorage implements UsersRelationStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(long userId, long friendId) {

        String sqlQuery = "INSERT INTO users_relations(primary_user_id, secondary_user_id, relation_accepted) " +
                "VALUES(?, ? , false)";

        KeyHolder kayHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setLong(1, userId);
            stmt.setLong(2, friendId);
            return stmt;
        }, kayHolder);
        log.debug("Добавлен новый друг");
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        String sqlQuery = "DELETE FROM users_relations WHERE primary_user_id = ? AND secondary_user_id = ? ";

        int numberRows = jdbcTemplate.update(sqlQuery, userId, friendId);

        if (numberRows > 1) {
            throw new DuplicateException(String.format("В таблице удалены больше одной записи с userId = %s и friendId = %s ", userId, friendId));
        }
        if (numberRows == 0) {
            int newNumberRows = jdbcTemplate.update(sqlQuery, friendId, userId);
            if (newNumberRows > 1) {
                throw new DuplicateException(String.format("В таблице удалены больше одной записи с userId = %s и friendId = %s ", friendId, userId));
            }
        }
        log.debug("Удален друг");
    }

    @Override
    public List<Long> getFriends(long userId) {
        String sqlQuery = "SELECT ur.PRIMARY_USER_ID AS friend " +
                "FROM USERS_RELATIONS ur " +
                "WHERE ur.SECONDARY_USER_ID = ?" +
                "AND ur.RELATION_ACCEPTED = TRUE " +
                "UNION " +
                "SELECT ur.SECONDARY_USER_ID AS friend " +
                "FROM USERS_RELATIONS ur " +
                "WHERE ur.PRIMARY_USER_ID = ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                        rs.getLong("friend"),
                userId, userId);
    }

    @Override
    public List<Long> getCommonFriends(long userId, long friendId) {
        List<Long> friends = getFriends(userId);
        List<Long> toFriends = getFriends(friendId);

        List<Long> community = new ArrayList<>();

        for (Long i : friends) {
            for (Long h : toFriends) {
                if (i.equals(h)) {
                    community.add(i);
                }
            }
        }
        return community;
    }


}
