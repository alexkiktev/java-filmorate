package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.utils.RowMapperUtils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, rate, mpa_id) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        addFilmGenre(film);
        log.info("The movie has been added to the database: " + film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film, Long id, Integer rate) {
        if (getFilm(id) != null) {
            String sqlQuery = "DELETE FROM filmgenre WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery, id);
            sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ? " +
                "WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    Date.valueOf(film.getReleaseDate()),
                    film.getDuration(),
                    rate,
                    film.getMpa().getId(),
                    id);
            List<Genre> genres = new ArrayList<>(film.getGenres()).stream().distinct().collect(Collectors.toList());
            film.setGenres(genres);
            addFilmGenre(film);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The film id " + id + " not found!");
        }
        log.info("The movie has been updated to the database: " + film.getName());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery =
            "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id, " +
                "m.name AS mpa_name " +
            "FROM films AS f " +
            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id";
        List<Film> films = jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToFilm);
        films.forEach(f -> f.setGenres(getGenres(f.getId())));
        return films;
    }

    @Override
    public Film getFilm(Long id) {
        String sqlQuery =
            "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id, " +
                "m.name AS mpa_name " +
            "FROM films AS f " +
            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "WHERE f.film_id = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToFilm, id);
        films.forEach(f -> f.setGenres(getGenres(f.getId())));
        if (films.size() != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The film id " + id + " not found!");
        }
        return films.get(0);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        String sqlQuery =
                "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id, " +
                    "m.name AS mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "ORDER BY f.rate DESC " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToFilm, count);
        films.forEach(f -> f.setGenres(getGenres(f.getId())));
        return films;
    }

    @Override
    public void addLike(Long id, Long userId) {
        if (getFilm(id) != null && userStorage.getUser(userId) != null) {
            String sqlQuery = "INSERT INTO userlikes (film_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, id, userId);
            sqlQuery = "UPDATE films SET rate = rate + 1 WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery, id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error request");
        }
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        if (getFilm(id) != null && userStorage.getUser(userId) != null) {
            String sqlQuery = "DELETE FROM userlikes WHERE film_id = ? AND user_id = ?";
            jdbcTemplate.update(sqlQuery, id, userId);
            sqlQuery = "UPDATE films SET rate = rate - 1 WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery, id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error request");
        }
    }

    private void addFilmGenre(Film film) {
        if (film.getGenres() != null) {
            String sqlQuery = "INSERT INTO filmgenre (film_id, genre_id) VALUES (?, ?)";
            List<Genre> genres = new ArrayList<>(film.getGenres());
            jdbcTemplate.batchUpdate(sqlQuery, genres, genres.size(),
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setLong(1, film.getId());
                        ps.setInt(2, genre.getId());
                    }
            );
        }
    }

    private List<Genre> getGenres(Long id) {
        String sqlQuery =
                "SELECT fg.genre_id, g.name " +
                "FROM filmgenre AS fg " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?";
        return jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToGenre, id);
    }

}
