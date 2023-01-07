package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.info("Received a request to create a user");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Received a request to update user data");
        return userService.updateUser(user, user.getId());
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Received a friend request");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("RA request was received to remove a user {} from the user's friends {}", friendId, id);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("A request for user {} data was received", id);
        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Request received for all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Request received for user's friends with id {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("A request was received for common friends of user {} and user {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

}
