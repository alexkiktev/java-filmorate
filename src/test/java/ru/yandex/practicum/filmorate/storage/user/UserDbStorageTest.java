package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.service.utils.ValidatorTestUtils.dtoHasErrorMessage;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    public final UserService userService;

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
        User user = getUser("mymail1@yandex.ru",
                "agk1",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        userService.createUser(user);
        User savedUser = userService.getUser(user.getId());
        Assertions.assertEquals(savedUser, user, "users don't match");
    }

    @Test
    void createUserBlankEmailTest() {
        User user = getUser("",
                "agk2",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        Assertions.assertTrue(dtoHasErrorMessage(userService.createUser(user), "email should not be blank"));
    }

    @Test
    void createUserBadEmailTest() {
        User user = getUser("mymailyandex.ru",
                "agk3",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        Assertions.assertTrue(dtoHasErrorMessage(userService.createUser(user),
                "Email address has invalid format: " + user.getEmail()));
    }

    @Test
    void createUserBlankLoginTest() {
        User user = getUser("mymail4@yandex.ru",
                "",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        Assertions.assertTrue(dtoHasErrorMessage(userService.createUser(user), "login should not be blank"));
    }

    @Test
    void createUserSpaceInLoginTest() {
        User user = getUser("mymail5@yandex.ru",
                "ag k5",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        Assertions.assertTrue(dtoHasErrorMessage(userService.createUser(user),
                "Bad formed person name: " + user.getLogin()));
    }

    @Test
    void createUserBlankNameTest() {
        User user = getUser("mymail6@yandex.ru",
                "agk",
                "",
                LocalDate.parse("1989-01-01"));
        userService.createUser(user);
        User savedUser = userService.getUser(user.getId());
        Assertions.assertEquals(savedUser.getName(), savedUser.getLogin(), "values don't match");
    }

    @Test
    void createUserNotBornTest() {
        User user = getUser("mymail7@yandex.ru",
                "agk7",
                "Aleksey",
                LocalDate.parse("2025-01-01"));
        Assertions.assertTrue(dtoHasErrorMessage(userService.createUser(user),
                "date of birth cannot be in the future"));
    }

    @Test
    void updateUserTest() {
        User user = getUser("mymail8@yandex.ru",
                "agk8",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        userService.createUser(user);

        String testName = "Алексей";

        user.setName(testName);
        userService.updateUser(user, user.getId());

        Assertions.assertEquals(user.getName(), testName, "names don't match");
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getUserTest() {
        User user = getUser("mymail9@yandex.ru",
                "agk9",
                "Aleksey",
                LocalDate.parse("1989-01-01"));
        userService.createUser(user);
        Long id = user.getId();
        Optional<User> userOptional = Optional.ofNullable(userService.getUser(id));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", id)
                                .hasFieldOrPropertyWithValue("login", "agk9")
                                .hasFieldOrPropertyWithValue("name", "Aleksey")
                );
    }

}