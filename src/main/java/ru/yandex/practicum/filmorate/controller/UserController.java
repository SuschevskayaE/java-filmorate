package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    private int id = 0;
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> usersAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        User newUser = validateUser(user);
        if (newUser.getId() == null) {
            newUser.setId(++id);
        } else {
            throw new ValidationException("Id должно быть null");
        }
        users.put(newUser.getId(), newUser);
        log.debug("Добавлен новый пользователь:" + newUser);
        return newUser;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Id " + user.getId() + " не найден");
        }
        User newUser = validateUser(user);
        users.put(newUser.getId(), newUser);
        log.debug("Пользователь обновлен: " + newUser);
        return newUser;
    }

    private User validateUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.debug("Заменено имя пользователя на логин: " + user.getName());
        }
        return user;
    }
}
