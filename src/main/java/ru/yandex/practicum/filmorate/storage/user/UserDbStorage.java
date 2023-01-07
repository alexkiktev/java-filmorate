package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.utils.RowMapperUtils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("User has been added to the database: " + user.getName());
        return user;
    }

    @Override
    public User updateUser(User user, Long id) {
        if (getUser(id) != null) {
            String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    Date.valueOf(user.getBirthday()),
                    id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user id " + id + " not found!");
        }
        log.info("User has been updated to the database: " + user.getName());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery =
                "SELECT u.user_id, u.email, u.login, u.name, u.birthday " +
                "FROM users AS u";
        return jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToUser);
    }

    @Override
    public User getUser(Long id) {
        String sqlQuery = "SELECT u.user_id, u.email, u.login, u.name, u.birthday FROM users AS u WHERE u.user_id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToUser, id);
        if (users.size() != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user id " + id + " not found!");
        }
        return users.get(0);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        if (getUser(id) != null && getUser(friendId) != null) {
            String sqlQuery = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, 1)";
            jdbcTemplate.update(sqlQuery, id, friendId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error request");
        }
        log.info("User " + getUser(id).getName() + " made friends with " + getUser(friendId).getName());
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        if (getUser(id) != null && getUser(friendId) != null) {
            String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sqlQuery, id, friendId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error request");
        }
        log.info("User " + getUser(id).getName() + " ended the friendship with " + getUser(friendId).getName());
    }

    @Override
    public List<User> getFriends(Long id) {
        if (getUser(id) != null) {
            String sqlQuery = "SELECT u.user_id, f.friend_id, u.email, u.login, u.name, u.birthday " +
                    "FROM friends AS f " +
                    "LEFT JOIN users AS u ON u.user_id = f.friend_id " +
                    "WHERE f.user_id = ?";
            return jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToUser, id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user id " + id + " not found!");
        }
    }

    @Override
    public List<User> getCommonFriends(Long id, Long friendId) {
        if (getUser(id) != null && getUser(friendId) != null) {
            String sqlQuery = "SELECT  u.user_id, u.email, u.login, u.name, u.birthday " +
                    "FROM users AS u " +
                    "JOIN friends AS f1 ON u.USER_ID = f1.FRIEND_ID " +
                    "JOIN friends AS f2 ON f1.FRIEND_ID = f2.FRIEND_ID " +
                    "WHERE f1.USER_ID = ? AND f2.USER_ID = ?";
            return jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToUser, id, friendId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error request");
        }
    }

}
