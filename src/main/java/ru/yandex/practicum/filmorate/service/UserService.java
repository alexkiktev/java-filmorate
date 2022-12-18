package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserService {

    final private List<User> users = new ArrayList<>();
    private Long id = 0L;

    public User createUser(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.add(user);
        log.info("Create user: " + user.getName());
        return user;
    }

    public User updateUser(User user, Long id) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        users.set(Math.toIntExact(id - 1), user);
        log.info("Create user: " + user.getName());
        return user;
    }

    public List<User> getAllUsers() {
        return users;
    }

}
