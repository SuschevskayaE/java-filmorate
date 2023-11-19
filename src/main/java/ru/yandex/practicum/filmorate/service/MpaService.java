package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

@Service
public class MpaService extends AbstractService<MpaRating> {

    @Autowired
    public MpaService(MpaRatingStorage mpaRatingStorage) {
        this.storage = mpaRatingStorage;
    }
}
