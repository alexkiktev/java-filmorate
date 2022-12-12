package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilmService {

    private List<Film> films = new ArrayList<>();
    private Long id = 0L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    //создание фильма
    public Film createFilm (@Valid @NotNull Film film) {

        //проверка даты релиза
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidException("The release date can't be earlier 1895-12-28");
        }

        film.setId(++id);
        films.add(film);
        logger.info("Create film: " + film.getName());
        return film;
    }

    //обновление фильма
    public Film updateFilm(Film film, Long id) {

        //проверка даты релиза
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidException("The release date can't be earlier 1895-12-28");
        }

        film.setId(id);
        films.set(Math.toIntExact(id - 1), film);
        logger.info("Update film: " + film.getName());
        return film;
    }

    //получение всех фильмов
    public List<Film> getAllFilms() {
        return films;
    }

}
