package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public User createUser(User user);

    public User updateUser(User user, Long id);

    public List<User> getAllUsers();

    public User getUser(Long id);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);

}
