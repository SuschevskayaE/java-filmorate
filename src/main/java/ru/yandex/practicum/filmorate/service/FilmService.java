package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService extends AbstractService<Film> {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addLike(Integer filmId, Integer userId) {
        User user = userStorage.get(userId);
        Film film = filmStorage.get(filmId);

        Set<Long> likes = film.getLikes();
        likes.add(user.getId());
        film.setLikes(likes);
        return film;
    }

    public Film removeLike(Integer filmId, Integer userId) {
        User user = userStorage.get(userId);
        Film film = filmStorage.get(filmId);

        Set<Long> likes = film.getLikes();
        likes.remove(user.getId());
        film.setLikes(likes);
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> films = filmStorage.getAll();

        return films.stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }
}
