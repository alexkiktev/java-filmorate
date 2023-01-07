package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {


    private final HashMap<Long, Film> films = new HashMap<>();
    private Long id = 0L;
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    private final UserStorage userStorage;

    public InMemoryFilmStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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
    public Film updateFilm(Film film, Long id, Integer rate) {
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
    public List<Film> getPopularFilms(Long count) {
        List<Film> filmList = new ArrayList<Film>(films.values());
        return filmList.stream()
                .sorted((f0, f1) -> f1.getUserLikes().size() - f0.getUserLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilm(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The film id " + id + " not found!");
        }
    }

    @Override
    public void addLike(Long id, Long userId) {
        Film film = getFilm(id);
        User user = userStorage.getUser(userId);
        film.getUserLikes().add(user.getId());
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        Film film = getFilm(id);
        User user = userStorage.getUser(userId);
        film.getUserLikes().remove(user.getId());
    }

}
