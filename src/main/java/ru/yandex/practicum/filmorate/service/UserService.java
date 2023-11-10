package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService extends AbstractService<User> {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addToFriend(Integer userId, Integer friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);

        Set<Long> friends = user.getIdFriends();
        friends.add(friend.getId());
        user.setIdFriends(friends);

        Set<Long> toFriends = friend.getIdFriends();
        toFriends.add(user.getId());
        friend.setIdFriends(toFriends);

        return user;
    }

    public User removeFromFriend(Integer userId, Integer friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);

        Set<Long> friends = user.getIdFriends();
        friends.remove(friend.getId());
        user.setIdFriends(friends);

        Set<Long> toFriends = friend.getIdFriends();
        toFriends.remove(user.getId());
        friend.setIdFriends(toFriends);

        return user;
    }

    public List<User> getFriends(Integer userId) {
        User user = userStorage.get(userId);
        Set<Long> friends = user.getIdFriends();

        List<User> users = userStorage.getAll();

        List<User> usersToFriend = new ArrayList<>();

        for (User u : users) {
            for (Long i : friends) {
                if (u.getId().equals(i)) {
                    usersToFriend.add(u);
                }
            }
        }
        return usersToFriend;
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);

        Set<Long> friends = user.getIdFriends();
        Set<Long> toFriends = friend.getIdFriends();

        Set<Long> community = new HashSet<>();

        for (Long i : friends) {
            for (Long h : toFriends) {
                if (i.equals(h)) {
                    community.add(i);
                }
            }
        }

        List<User> users = userStorage.getAll();
        List<User> usersToFriend = new ArrayList<>();

        for (Long c : community) {
            for (User u : users) {
                if (u.getId().equals(c)) {
                    usersToFriend.add(u);
                }
            }
        }
        return usersToFriend;
    }
}
