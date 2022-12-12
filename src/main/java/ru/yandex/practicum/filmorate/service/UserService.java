package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<User> users = new ArrayList<>();
    private Long id = 0L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public User createUser(User user) {

        //если имя пустое, то использовать логин

        String name = user.getName();

        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(++id);
        users.add(user);
        logger.info("Create user: " + user.getName());
        return user;
    }

    public User updateUser(User user, Long id) {

        //если имя пустое, то использовать логин
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(id);
        users.set(Math.toIntExact(id - 1), user);
        logger.info("Create user: " + user.getName());
        return user;
    }

    public List<User> getAllUsers() {
        return users;
    }

}
