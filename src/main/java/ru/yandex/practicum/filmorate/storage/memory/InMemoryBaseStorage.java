package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
@Component
@Slf4j
public abstract class InMemoryBaseStorage<T extends BaseUnit> implements AbstractStorage<T> {

    private final Map<Long, T> storage = new HashMap<>();
    private long id;

    public T create(T data) {
        data = validate(data);
        if (data.getId() == null) {
            data.setId(++id);
        } else {
            throw new ValidationException("Id должно быть null");
        }
        storage.put(data.getId(), data);
        log.debug("Добавлен новый элемент':" + data);
        return data;
    }

    public T update(T data) {
        if (!storage.containsKey(data.getId())) {
            throw new DataNotFoundException(String.format("Элемент %s не найден", data));
        }
        data = validate(data);
        storage.put(data.getId(), data);
        log.debug("Элемент обновлен:" + data);
        return data;
    }

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public T get(long id) {
        if (!storage.containsKey(id)) {
            throw new DataNotFoundException(String.format("Элемент с id %s не найден", id));
        }
        return storage.get(id);
    }

    public abstract T validate(T data);
}
