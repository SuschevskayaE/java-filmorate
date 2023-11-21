package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractService<T extends BaseUnit> {

    protected long generatedId = 0L;

    protected AbstractStorage<T> storage;

    public List<T> getAll() {
        return new ArrayList<>(storage.getAll());
    }

    public T get(long id) {
        return storage.get(id);
    }

    public T create(T data) {
        return storage.create(data);
    }

    public T update(T data) {
        return storage.update(data);
    }


}
