package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage extends InMemoryBaseStorage<User> implements UserStorage {

    @Override
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    public User create(User data) {
        if (data.getIdFriends() == null) {
            data.setIdFriends(new HashSet<>());
        }
        return super.create(data);
    }

    @Override
    public User update(User data) {
        if (data.getIdFriends() == null) {
            data.setIdFriends(new HashSet<>());
        }
        return super.update(data);
    }

    @Override
    public User get(long id) {
        return super.get(id);
    }

    @Override
    public User validate(User data) {
        if (data.getName().isBlank()) {
            data.setName(data.getLogin());
            log.debug("Заменено имя пользователя на логин: " + data.getName());
        }
        return data;
    }
}
