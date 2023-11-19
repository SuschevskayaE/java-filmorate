package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.sql.PreparedStatement;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(long filmId, long userId) {

        String sqlQuery = "INSERT INTO likes(film_id, user_id) " +
                "VALUES(?, ?)";

        KeyHolder kayHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setLong(1, filmId);
            stmt.setLong(2, userId);
            return stmt;
        }, kayHolder);
        log.debug("Добавлен новый лайк");
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ? ";

        int numberRows = jdbcTemplate.update(sqlQuery, filmId, userId);

        if (numberRows == 0) {
            throw new DataNotFoundException(String.format("В таблице нет одной записи с filmId = %s и userId = %s ", filmId, userId));
        }
        if (numberRows > 1) {
            throw new DuplicateException(String.format("В таблице удалены больше одной записи с filmId = %s и userId = %s ", filmId, userId));
        }
    }

    @Override
    public List<Long> getPopularFilms(int count) {

        String sqlQuery = "SELECT f.ID AS FILM_ID ," +
                "COUNT( DISTINCT l.USER_ID) AS popular " +
                "FROM FILMS f " +
                "left JOIN LIKES l ON l.FILM_ID = f.ID " +
                "GROUP BY FILM_ID " +
                "ORDER BY popular DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                        rs.getLong("film_id")
                , count);
    }

}
