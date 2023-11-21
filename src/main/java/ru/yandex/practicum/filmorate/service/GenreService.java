package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Service
public class GenreService extends AbstractService<Genre> {

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.storage = genreStorage;
    }

}
