package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends AbstractStorage<Film> {

    Film create(Film data);

    Film update(Film data);

    List<Film> getAll();

    Film get(long id);

    Film validate(Film data);
}
