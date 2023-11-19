package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikesStorage {

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Long> getPopularFilms(int count);
}
