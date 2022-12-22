package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user, Long id) {
        return userStorage.updateUser(user, id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public void addFriend(Long id, Long friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        //обоюдное добавление в друзья
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }

    public void deleteFriend(Long id, Long friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        //обоюдное удаление из друзей
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    public List<User> getFriends(Long id) {
        return getUser(id).getFriends()
                .stream()
                .map(o -> getUser(o))
                .collect(Collectors.toList());
    }

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
