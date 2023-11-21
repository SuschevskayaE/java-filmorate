package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query("select * from films", FilmDbStorage::createFilm);
    }

    @Override
    public Film get(long id) {
        try {
            return jdbcTemplate.queryForObject("select * from films where id = ?", FilmDbStorage::createFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException(String.format("В таблице нет одной записи с id = %s", id));
        }
    }

    @Override
    public Film create(Film data) {
        validate(data);

        String sqlQuery = "INSERT INTO films(name, description, release_date, duration, mpa_id) " +
                "VALUES(?, ?, ?,?, ?)";

        KeyHolder kayHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, data.getName());
            stmt.setString(2, data.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(data.getReleaseDate().atStartOfDay()));
            stmt.setInt(4, data.getDuration());
            stmt.setLong(5, data.getMpa().getId());
            return stmt;
        }, kayHolder);
        data.setId(kayHolder.getKey().longValue());
        log.debug("Добавлен новый элемент':" + data);

        return data;
    }

    @Override
    public Film update(Film data) {
        if (data.getId() == null) {
            throw new DataNotFoundException(String.format("Элемент %s не найден", data));
        }
        long id = get(data.getId()).getId();
        validate(data);

        String sqlQuery = "UPDATE films " +
                "SET " +
                "name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_id = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery,
                data.getName(),
                data.getDescription(),
                data.getReleaseDate(),
                data.getDuration(),
                data.getMpa().getId(),
                id);
        log.debug("Элемент обновлен:" + data);
        return data;
    }


    @Override
    public Film validate(Film data) {
        if (data.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            log.debug(String.format("Дата релиза раньше %s", START_RELEASE_DATE));
            throw new ValidationException(String.format("Дата релиза раньше %s", START_RELEASE_DATE));
        }
        return data;
    }

    static Film createFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getTimestamp("release_date").toLocalDateTime().toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(MpaRating.builder().id(rs.getLong("mpa_id")).build())
                .build();
    }
}
