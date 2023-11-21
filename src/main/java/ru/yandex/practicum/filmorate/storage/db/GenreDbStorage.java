package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("select * from genres", GenreDbStorage::createGenre);
    }

    @Override
    public Genre get(long id) {
        try {
            return jdbcTemplate.queryForObject("select * from genres where id = ?", GenreDbStorage::createGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException(String.format("В таблице нет одной записи с id = %s", id));
        }
    }

    public List<Genre> getGenreIdsForFilmId(long filmId) {
        String sqlQuery = "SELECT g.ID , g.NAME " +
                "FROM FILM_GENRES fg " +
                "LEFT JOIN FILMS f ON fg.FILM_ID = f.ID " +
                "LEFT JOIN GENRES g ON fg.GENRE_ID =g.ID " +
                "WHERE f.ID = ?";

        return jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre, filmId);
    }

    public void createGenreByFilm(long genreId, long filmId) {
        String sqlQuery = "MERGE INTO film_genres(genre_id, film_id) " +
                "VALUES(?, ?)";

        jdbcTemplate.update(sqlQuery,
                genreId,
                filmId);
        log.debug("Добавлен новый элемент'");
    }

    public void deleteAllGenreByFilm(long filmId) {
        String sqlQuery = "DELETE FROM film_genres WHERE film_id = ?";

        int count = jdbcTemplate.update(sqlQuery,
                filmId);
        log.debug(String.format("Удалены %s элементов'", count));
    }

    @Override
    public Genre create(Genre data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Genre update(Genre data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Genre validate(Genre data) {
        throw new UnsupportedOperationException();
    }

    static Genre createGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
