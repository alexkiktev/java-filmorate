package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.service.utils.ValidatorTestUtils.dtoHasErrorMessage;

class UserServiceTest {

    private UserService userService = new UserService();

    private static User getUser(String email, String login, String name, LocalDate birthday) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }

    @Test
    void createUserTest() {
        User user = getUser("mymail@yandex.ru",
                "agk",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        Assertions.assertEquals(userService.createUser(user), user, "users don't match");
    }

    @Test
    void createUserBlankEmailTest() {
        User user = getUser("",
                "agk",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        Assertions.assertTrue(dtoHasErrorMessage(user, "email should not be blank"));
    }

    @Test
    void createUserBadEmailTest() {
        User user = getUser("mymailyandex.ru",
                "agk",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        Assertions.assertTrue(dtoHasErrorMessage(user,
                "Email address has invalid format: " + user.getEmail()));
    }

    @Test
    void createUserBlankLoginTest() {
        User user = getUser("mymail@yandex.ru",
                "",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        Assertions.assertTrue(dtoHasErrorMessage(user, "login should not be blank"));
    }

    @Test
    void createUserSpaceInLoginTest() {
        User user = getUser("mymail@yandex.ru",
                "ag k",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        Assertions.assertTrue(dtoHasErrorMessage(user, "Bad formed person name: " + user.getLogin()));
    }

    @Test
    void createUserBlankNameTest() {
        User user = getUser("mymail@yandex.ru",
                "agk",
                "",
                LocalDate.parse("1989-01-01"));
        userService.createUser(user);
        Assertions.assertEquals(user.getName(), user.getLogin(), "values don't match");
    }

    @Test
    void createUserNotBornTest() {
        User user = getUser("mymail@yandex.ru",
                "agk",
                "Aleksey",
                LocalDate.parse("2025-01-01"));
        Assertions.assertTrue(dtoHasErrorMessage(user, "date of birth cannot be in the future"));
    }

    @Test
    void updateUserTest() {
        User user = getUser("mymail@yandex.ru",
                "agk",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        userService.createUser(user);

        String testName = "Алексей";

        user.setName(testName);
        userService.updateUser(user, user.getId());

        Assertions.assertEquals(user.getName(), testName, "names don't match");
    }

}