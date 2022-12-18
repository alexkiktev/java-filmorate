package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.service.utils.ValidatorTestUtils.dtoHasErrorMessage;

class FilmServiceTest {

    private FilmService filmService = new FilmService();

    private static Film getFilm(String name, String description, LocalDate releaseDate, Integer duration) {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);
        return film;
    }

    @Test
    void createFilmTest() {
        Film film = getFilm("Матрица",
                "Описание фильма",
                LocalDate.parse("2020-08-20"),
                90);
        Assertions.assertEquals(filmService.createFilm(film), film, "films don't match");
    }

    @Test
    void createFilmBlankNameTest() {
        Film film = getFilm(" ",
                "Фильм про Новый год",
                LocalDate.parse("2000-05-10"),
                90);
        Assertions.assertTrue(dtoHasErrorMessage(film, "name should not be blank"));
    }

    @Test
    void createFilmTooLongDescriptionTest() {
        Film film = getFilm("Ирония судьбы",
                "Очень очень очень очень очень очень очень очень очень очень очень очень очень очень " +
                        "очень очень очень очень очень очень очень очень очень очень очень очень очень очень " +
                        "очень очень очень длинное описание - 217 символов",
                LocalDate.parse("2000-05-10"),
                90);
        Assertions.assertTrue(dtoHasErrorMessage(film,
                "the description should be no more than 200 characters"));
    }

    @Test
    void createFilmBadDateTest() {
        Film film = getFilm("Как я основал город на болоте",
                "Описание фильма",
                LocalDate.parse("1703-05-27"),
                90);
        ValidException ex = assertThrows(
                ValidException.class,
                () -> filmService.createFilm(film)
        );
        assertEquals("The release date can't be earlier 1895-12-28", ex.getMessage());
    }

    @Test
    void createFilmNegativeDurationTest() {
        Film film = getFilm("Матрица: Перезагрузка",
                "Описание фильма",
                LocalDate.parse("2000-05-10"),
                -10);
        Assertions.assertTrue(dtoHasErrorMessage(film, "the duration should be positive"));
    }

    @Test
    void updateFilmTest() {
        Film film = getFilm("Матрица",
                "Описание фильма",
                LocalDate.parse("2020-08-20"),
                90);
        filmService.createFilm(film);

        String testDescription = "Новое описание фильма";
        Integer testDuration = 150;

        film.setDescription(testDescription);
        film.setDuration(testDuration);
        filmService.updateFilm(film, film.getId());

        Assertions.assertEquals(film.getDescription(), testDescription, "descriptions don't match");
        Assertions.assertEquals(film.getDuration(), testDuration, "durations don't match");
    }

}