package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    public Film createFilm (Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidException("The release date can't be earlier 1895-12-28");
        }
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film, Long id) {
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidException("The release date can't be earlier 1895-12-28");
        }
        Integer rate = film.getRate();
        return filmStorage.updateFilm(film, id, rate);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    public void addLike(Long id, Long userId) {
        filmStorage.addLike(id, userId);

    }

    public void deleteLike(Long id, Long userId) {
        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getPopularFilms(Long count) {
        List<Film> films = filmStorage.getPopularFilms(count);
        return films;
    }

}
