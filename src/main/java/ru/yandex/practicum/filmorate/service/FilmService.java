package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService extends AbstractService<Film> {

    private final LikesStorage likesStorage;
    private final MpaRatingStorage mpaRatingStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       LikesStorage likesStorage,
                       MpaRatingStorage mpaRatingStorage,
                       GenreStorage genreStorage) {
        this.storage = filmStorage;
        this.likesStorage = likesStorage;
        this.mpaRatingStorage = mpaRatingStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = storage.getAll();
        List<MpaRating> mpa = mpaRatingStorage.getAll();
        List<Film> fullFilms = new ArrayList<>();

        for (Film film : films) {
            long mpaId = film.getMpa().getId();

            MpaRating mpaRating = mpa.stream().filter(m -> m.getId().equals(mpaId)).findFirst()
                    .orElseThrow(() -> new DataNotFoundException(String.format("Элемент c id %s не найден", mpaId)));
            film.setMpa(mpaRating);
            film.setGenres(genreStorage.getGenreIdsForFilmId(film.getId()));
            fullFilms.add(film);
        }
        return fullFilms;
    }

    @Override
    public Film get(long id) {
        Film film = storage.get(id);
        film.setGenres(genreStorage.getGenreIdsForFilmId(id));
        film.setMpa(mpaRatingStorage.get(film.getMpa().getId()));
        return film;
    }

    @Override
    public Film create(Film data) {
        Film film = storage.create(data);
        saveGenresByFilm(film);
        return get(film.getId());
    }

    @Override
    public Film update(Film data) {
        Film film = storage.update(data);
        saveGenresByFilm(film);
        return get(film.getId());
    }

    public Film addLike(long filmId, long userId) {
        likesStorage.addLike(filmId, userId);
        return get(filmId);
    }

    public Film removeLike(long filmId, long userId) {
        likesStorage.removeLike(filmId, userId);
        return get(filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Long> filmIds = likesStorage.getPopularFilms(count);
        List<Film> filmsAll = getAll();

        List<Film> films = new ArrayList<>();

        for (Long id : filmIds) {
            Film film = filmsAll.stream().filter(f -> f.getId().equals(id)).findFirst()
                    .orElseThrow(() -> new DataNotFoundException(String.format("Элемент c id %s не найден", id)));
            films.add(film);
        }
        return films;

    }

    private void saveGenresByFilm(Film data) {
        List<Genre> genres = data.getGenres();
        if (genres != null) {
            genreStorage.deleteAllGenreByFilm(data.getId());
            for (Genre genre : genres) {
                genreStorage.createGenreByFilm(genre.getId(), data.getId());
            }
        }
    }
}
