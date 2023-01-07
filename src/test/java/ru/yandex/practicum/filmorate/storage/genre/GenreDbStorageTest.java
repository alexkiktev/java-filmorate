package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

    public final GenreDbStorage genreDbStorage;

    @Test
    void getGenreTest() {
        Optional<Genre> mpaOptional = Optional.ofNullable(genreDbStorage.getGenre(2));
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa)
                                .hasFieldOrPropertyWithValue("id", 2)
                                .hasFieldOrPropertyWithValue("name", "Драма")
                );
    }

    @Test
    void getAllGenreTest() {
        assertEquals(6, genreDbStorage.getAllGenre().size());
    }

    @Test
    void getNotExistGenreTest() {
        assertThrows(ResponseStatusException.class, () -> genreDbStorage.getGenre(9999));
    }

}