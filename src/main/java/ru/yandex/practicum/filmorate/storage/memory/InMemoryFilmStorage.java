package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Deprecated
@Component
@Slf4j
public class InMemoryFilmStorage extends InMemoryBaseStorage<Film> implements FilmStorage {

    private static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    public List<Film> getAll() {
        return super.getAll();
    }

    @Override
    public Film create(Film data) {
        return super.create(data);
    }

    @Override
    public Film update(Film data) {
        return super.update(data);
    }

    @Override
    public Film get(long id) {
        return super.get(id);
    }

    @Override
    public Film validate(Film data) {
        if (data.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            log.debug(String.format("Дата релиза раньше %s", START_RELEASE_DATE));
            throw new ValidationException(String.format("Дата релиза раньше %s", START_RELEASE_DATE));
        }
        return data;
    }
}
