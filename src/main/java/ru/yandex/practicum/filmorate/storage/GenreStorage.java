package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage extends AbstractStorage<Genre> {

    List<Genre> getGenreIdsForFilmId(long filmId);

    void createGenreByFilm(long genreId, long filmId);

    void deleteAllGenreByFilm(long filmId);
}
