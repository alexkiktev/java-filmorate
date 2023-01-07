package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film createFilm (Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film, Long id) {
        return filmStorage.updateFilm(film, id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    public void addLike(Long id, Long userId) {
        Film film = getFilm(id);
        User user = userStorage.getUser(userId);
        film.getUserLikes().add(user.getId());
    }

    public void deleteLike(Long id, Long userId) {
        Film film = getFilm(id);
        User user = userStorage.getUser(userId);
        film.getUserLikes().remove(user.getId());
    }

    public List<Film> getPopularFilms(Long count) {
        HashMap<Long, Film> films = filmStorage.getAllFilmsHashMap();
        return films.values()
                .stream()
                .sorted((f0, f1) -> f1.getUserLikes().size() - f0.getUserLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}
