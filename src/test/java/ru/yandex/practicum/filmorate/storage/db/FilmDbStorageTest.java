package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void getAllFilmTrue() {
        Film newFilm = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2023, 10, 2))
                .duration(212)
                .mpa(MpaRating.builder().id(1L).name(null).build())
                .build();

        Film newFilm2 = Film.builder()
                .name("name2")
                .description("description2")
                .releaseDate(LocalDate.of(1999, 10, 2))
                .duration(12)
                .mpa(MpaRating.builder().id(3L).name(null).build())
                .build();

        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        long id1 = filmStorage.create(newFilm).getId();
        long id2 = filmStorage.create(newFilm2).getId();

        List<Film> savedFilms = filmStorage.getAll();

        assertThat(savedFilms.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
        assertThat(savedFilms.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm2);
    }

    @Test
    public void createFilmAndGetFilmByIdTrue() {
        Film newFilm = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2023, 10, 2))
                .duration(212)
                .mpa(MpaRating.builder().id(1L).name(null).build())
                .build();

        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        long id = filmStorage.create(newFilm).getId();

        Film savedFilm = filmStorage.get(id);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void updateFilmTrue() {
        Film newFilm = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2023, 10, 2))
                .duration(212)
                .mpa(MpaRating.builder().id(1L).name(null).build())
                .build();

        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        long id = filmStorage.create(newFilm).getId();

        newFilm.setId(id);
        newFilm.setName("newName");
        newFilm.setDescription("NewDescription");
        newFilm.setReleaseDate(LocalDate.of(1999, 10, 2));
        newFilm.setDuration(10);
        newFilm.setMpa(MpaRating.builder().id(2L).name(null).build());

        filmStorage.update(newFilm);

        Film savedFilm = filmStorage.get(id);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

}
