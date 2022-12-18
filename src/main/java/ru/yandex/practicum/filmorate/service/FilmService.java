package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FilmService {

    private List<Film> films = new ArrayList<>();
    private Long id = 0L;

    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    public Film createFilm (@Valid @NotNull Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidException("The release date can't be earlier 1895-12-28");
        }
        film.setId(++id);
        films.add(film);
        log.info("Create film: " + film.getName());
        return film;
    }

    public Film updateFilm(Film film, Long id) {
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidException("The release date can't be earlier 1895-12-28");
        }
        film.setId(id);
        films.set(Math.toIntExact(id - 1), film);
        log.info("Update film: " + film.getName());
        return film;
    }

    public List<Film> getAllFilms() {
        return films;
    }

}
