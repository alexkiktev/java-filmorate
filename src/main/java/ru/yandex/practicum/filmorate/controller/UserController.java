package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService userService = new UserService();

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        userService.createUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        userService.updateUser(user, user.getId());
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
