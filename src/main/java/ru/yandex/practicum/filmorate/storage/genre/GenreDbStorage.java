package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.utils.RowMapperUtils;

import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenre() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToGenre);
    }

    @Override
    public Genre getGenre(Integer id) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToGenre, id);
        if (genres.size() != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The genre id " + id + " not found!");
        }
        return genres.get(0);
    }
}
