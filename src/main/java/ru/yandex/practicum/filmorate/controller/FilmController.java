package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private int id = 0;
    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> filmsAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        if (film.getId() == null) {
            film.setId(++id);
        } else {
            throw new ValidationException("Id должно быть null");
        }
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм:" + film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Id " + film.getId() + " не найден");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.debug("Фильм обновлен:" + film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.debug("Ошибка: Дата релиза раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 года");
        }
    }
}
