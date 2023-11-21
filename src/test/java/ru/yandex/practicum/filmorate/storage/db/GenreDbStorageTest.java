package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void getAllGenreTrue() {
        List<Genre> genres = new ArrayList<>();
        genres.add(Genre.builder()
                .id(1L)
                .name("Комедия")
                .build());
        genres.add(Genre.builder()
                .id(2L)
                .name("Драма")
                .build());
        genres.add(Genre.builder()
                .id(6L)
                .name("Боевик")
                .build());

        GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        List<Genre> savedGenres = genreStorage.getAll();

        assertThat(savedGenres.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genres.get(0));
        assertThat(savedGenres.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genres.get(1));
        assertThat(savedGenres.get(5))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genres.get(2));
    }

    @Test
    public void getGenreByIdTrue() {
        Genre newGenre = Genre.builder()
                .id(1L)
                .name("Комедия")
                .build();

        GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        Genre savedGenres = genreStorage.get(1);

        assertThat(savedGenres)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newGenre);
    }

    @Test
    public void createGenreFalse() {
        Genre newGenre = Genre.builder()
                .id(1L)
                .name("Genre 1")
                .build();
        GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);

        final UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> genreStorage.create(newGenre)
        );
    }

    @Test
    public void updateGenreFalse() {
        Genre newGenre = Genre.builder()
                .id(1L)
                .name("Genre 1")
                .build();
        GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);

        final UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> genreStorage.update(newGenre)
        );
    }
}
