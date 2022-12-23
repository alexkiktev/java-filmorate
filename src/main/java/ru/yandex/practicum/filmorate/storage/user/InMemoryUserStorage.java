package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private Long id= 0L;

    @Override
    public User createUser(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Create user: " + user.getName());
        return user;
    }

    @Override
    public User updateUser(User user, Long id) {
        if (users.containsKey(user.getId())) {
            if (StringUtils.isBlank(user.getName())) {
                user.setName(user.getLogin());
            }
            users.put(id, user);
            log.info("Update user: " + user.getName());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user id " + id + " not found!");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user id " + id + " not found!");
        }
    }

}
