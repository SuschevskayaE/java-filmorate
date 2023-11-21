package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaRatingDbStorage implements MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MpaRating> getAll() {
        return jdbcTemplate.query("select * from mpa_ratings", MpaRatingDbStorage::createMpaRating);
    }

    @Override
    public MpaRating get(long id) {
        try {
            return jdbcTemplate.queryForObject("select * from mpa_ratings where id = ?", MpaRatingDbStorage::createMpaRating, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException(String.format("В таблице нет одной записи с id = %s", id));
        }
    }

    @Override
    public MpaRating create(MpaRating data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MpaRating update(MpaRating data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MpaRating validate(MpaRating data) {
        throw new UnsupportedOperationException();
    }

    static MpaRating createMpaRating(ResultSet rs, int rowNum) throws SQLException {
        return MpaRating.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
