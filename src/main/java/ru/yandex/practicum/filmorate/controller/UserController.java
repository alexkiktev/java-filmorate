package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private UserService userService = new UserService();

    //создание пользователя
    @PostMapping(value = "/user")
    public User createUser(@RequestBody @Valid User user) {
        userService.createUser(user);
        return user;
    }

    //обновление пользователя
    @PutMapping(value = "/user/{id}")
    public User updateUser(@RequestBody @Valid User user, @PathVariable Long id) {
        userService.updateUser(user, id);
        return user;
    }

    //получение списка всех пользователей
    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
