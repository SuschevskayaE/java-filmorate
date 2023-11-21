package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface UsersRelationStorage {

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    List<Long> getFriends(long userId);

    List<Long> getCommonFriends(long userId, long friendId);
}
