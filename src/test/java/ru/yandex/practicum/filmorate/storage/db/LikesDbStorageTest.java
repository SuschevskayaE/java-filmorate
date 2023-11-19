package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikesDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private Long filmId;
    private Long userId;

    @BeforeEach
    public void setUp() {
        Film newFilm = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2023, 10, 2))
                .duration(212)
                .mpa(MpaRating.builder().id(1L).name(null).build())
                .build();

        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmId = filmStorage.create(newFilm).getId();

        User newUser = User.builder()
                .email("email@y.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1999, 10, 2))
                .build();

        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        userId = userStorage.create(newUser).getId();
    }

    @Test
    public void addLikeTrue() {

        LikesStorage likesStorage = new LikesDbStorage(jdbcTemplate);
        likesStorage.addLike(filmId, userId);

        String sqlQuery = "SELECT * " +
                "FROM LIKES l " +
                "WHERE film_id = ? " +
                "AND  user_id = ?";

        List<Long> like = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                        rs.getLong("id"),
                filmId, userId);
        assertThat(like)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    public void removeLikeTrue() {

        LikesStorage likesStorage = new LikesDbStorage(jdbcTemplate);
        likesStorage.addLike(filmId, userId);
        likesStorage.removeLike(filmId, userId);

        String sqlQuery = "SELECT * " +
                "FROM LIKES l " +
                "WHERE film_id = ? " +
                "AND  user_id = ?";

        List<Long> like = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                        rs.getLong("id"),
                filmId, userId);
        assertThat(like)
                .isNullOrEmpty();
    }

    @Test
    public void getPopularFilmsTrue() {

        LikesStorage likesStorage = new LikesDbStorage(jdbcTemplate);
        likesStorage.addLike(filmId, userId);

        List<Long> popularFilms = likesStorage.getPopularFilms(1);

        assertThat(popularFilms)
                .isNotNull()
                .containsOnlyOnce(filmId);
    }
}
