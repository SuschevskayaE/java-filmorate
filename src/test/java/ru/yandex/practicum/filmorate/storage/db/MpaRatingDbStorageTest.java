package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaRatingDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void getAllMpaRatingTrue() {
        List<MpaRating> mpaRatings = new ArrayList<>();
        mpaRatings.add(MpaRating.builder()
                .id(1L)
                .name("G")
                .build());
        mpaRatings.add(MpaRating.builder()
                .id(2L)
                .name("PG")
                .build());
        mpaRatings.add(MpaRating.builder()
                .id(5L)
                .name("NC-17")
                .build());

        MpaRatingStorage mpaRatingStorage = new MpaRatingDbStorage(jdbcTemplate);
        List<MpaRating> savedMpas = mpaRatingStorage.getAll();

        assertThat(savedMpas.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mpaRatings.get(0));
        assertThat(savedMpas.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mpaRatings.get(1));
        assertThat(savedMpas.get(4))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mpaRatings.get(2));
    }

    @Test
    public void getMpaRatingByIdTrue() {
        MpaRating newMpaRating = MpaRating.builder()
                .id(1L)
                .name("G")
                .build();

        MpaRatingStorage mpaRatingStorage = new MpaRatingDbStorage(jdbcTemplate);
        MpaRating savedMpaRatings = mpaRatingStorage.get(1);

        assertThat(savedMpaRatings)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newMpaRating);
    }

    @Test
    public void createMpaRatingFalse() {
        MpaRating newMpaRating = MpaRating.builder()
                .id(1L)
                .name("G")
                .build();

        MpaRatingStorage mpaRatingStorage = new MpaRatingDbStorage(jdbcTemplate);

        final UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> mpaRatingStorage.create(newMpaRating)
        );
    }

    @Test
    public void updateMpaRatingFalse() {
        MpaRating newMpaRating = MpaRating.builder()
                .id(1L)
                .name("G")
                .build();

        MpaRatingStorage mpaRatingStorage = new MpaRatingDbStorage(jdbcTemplate);

        final UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> mpaRatingStorage.update(newMpaRating)
        );
    }
}
