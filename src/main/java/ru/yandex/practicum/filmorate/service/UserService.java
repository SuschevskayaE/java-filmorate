package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.UsersRelationStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<User> {

    private final UsersRelationStorage usersRelationStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage, UsersRelationStorage usersRelationStorage) {
        this.storage = storage;
        this.usersRelationStorage = usersRelationStorage;

    }

    public User addToFriend(long userId, long friendId) {
        storage.get(userId);
        storage.get(friendId);
        usersRelationStorage.addFriend(userId, friendId);
        return storage.get(userId);
    }

    public User removeFromFriend(long userId, long friendId) {
        usersRelationStorage.removeFriend(userId, friendId);
        return storage.get(userId);
    }

    public List<User> getFriends(long userId) {
        List<Long> friendIds = usersRelationStorage.getFriends(userId);
        List<User> usersAll = storage.getAll();
        return enrichmentDataById(friendIds, usersAll);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        List<Long> friendIds = usersRelationStorage.getCommonFriends(userId, friendId);
        List<User> usersAll = storage.getAll();
        return enrichmentDataById(friendIds, usersAll);
    }

    private List<User> enrichmentDataById(List<Long> ids, List<User> data) {
        List<User> users = new ArrayList<>();
        for (Long id : ids) {
            User user = data.stream().filter(u -> u.getId().equals(id)).findFirst()
                    .orElseThrow(() -> new DataNotFoundException(String.format("Элемент c id %s не найден", id)));
            users.add(user);
        }
        return users;
    }
}
