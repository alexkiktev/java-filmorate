package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film, Long id);

    List<Film> getAllFilms();

    Film getFilm(Long id);

    HashMap<Long, Film> getAllFilmsHashMap();

}
