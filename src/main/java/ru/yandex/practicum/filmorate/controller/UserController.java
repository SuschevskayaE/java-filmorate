package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public List<User> usersAll() {
        return userStorage.getAll();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        return userStorage.get(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriend(@PathVariable int id,
                            @PathVariable int friendId) {
        return userService.addToFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeToFriend(@PathVariable int id,
                               @PathVariable int friendId) {
        return userService.removeFromFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id,
                                       @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
