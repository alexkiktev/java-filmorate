package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {

    private FilmService filmService = new FilmService();

    //добавление фильма
    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody @Valid Film film) {
        filmService.createFilm(film);
        return film;
    }

    //обновление фильма
    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody @Valid Film film) {
        filmService.updateFilm(film, film.getId());
        return film;
    }

    //получение всех фильмов
    @GetMapping(value = "/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

}
