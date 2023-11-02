package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class UserService {
    //добавление в друзья, удаление из друзей,
    // вывод списка общих друзей.
    // Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
    // То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.

    public User addToFriend(User user, Integer idFriend){
        return user;
    }

    public User removeFromFriend(User user, Integer idFriend){
        return user;
    }
}
