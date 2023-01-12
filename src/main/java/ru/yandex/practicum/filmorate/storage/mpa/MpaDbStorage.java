package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.utils.RowMapperUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToMpa);
    }

    @Override
    public Mpa getMpa(Integer id) {
        String sqlQuery = "SELECT * FROM mpa WHERE mpa_id = ?";
        List<Mpa> mpas = jdbcTemplate.query(sqlQuery, RowMapperUtils::mapRowToMpa, id);
        if (mpas.size() != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The MPA id " + id + " not found!");
        }
        return mpas.get(0);
    }
}
