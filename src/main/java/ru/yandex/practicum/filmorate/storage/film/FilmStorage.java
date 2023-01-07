package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film, Long id, Integer rate);

    List<Film> getAllFilms();

    Film getFilm(Long id);

    List<Film> getPopularFilms(Long count);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

}
