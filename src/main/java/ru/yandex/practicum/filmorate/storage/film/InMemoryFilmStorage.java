package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    private Long id = 0L;
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    @Override
    public Film createFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidException("The release date can't be earlier 1895-12-28");
        }
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Create film: " + film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film, Long id) {
        if (films.containsKey(film.getId())) {
            if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
                throw new ValidException("The release date can't be earlier 1895-12-28");
            }
            films.put(id, film);
            log.info("Update film: " + film.getName());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The film id " + id + " not found!");
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public HashMap<Long, Film> getAllFilmsHashMap() {
        return films;
    }

    @Override
    public Film getFilm(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The film id " + id + " not found!");
        }
    }

}
