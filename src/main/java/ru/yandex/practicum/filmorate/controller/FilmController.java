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
    @PostMapping(value = "/film")
    public Film createFilm(@RequestBody @Valid Film film) {
        filmService.createFilm(film);
        return film;
    }

    //обновление фильма
    @PutMapping(value = "/film/{id}")
    public Film updateFilm(@RequestBody @Valid Film film, @PathVariable Long id) {
        filmService.updateFilm(film, id);
        return film;
    }

    //получение всех фильмов
    @GetMapping(value = "/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

}
