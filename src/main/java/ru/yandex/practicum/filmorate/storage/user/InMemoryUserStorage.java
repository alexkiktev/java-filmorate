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
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users;
    private Long id= 0L;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public User createUser(User user) {
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

    @Override
    public void addFriend(Long id, Long friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        //обоюдное добавление в друзья
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        //обоюдное удаление из друзей
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    @Override
    public List<User> getFriends(Long id) {
        return getUser(id).getFriends()
                .stream()
                .map(o -> getUser(o))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = getUser(id);
        User friend = getUser(otherId);
        List<Long> commonFriends = new ArrayList<>(user.getFriends());
        commonFriends.retainAll(friend.getFriends());
        return commonFriends
                .stream()
                .map(o -> getUser(o))
                .collect(Collectors.toList());
    }

}
