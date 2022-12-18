package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private Long id;

    @NotBlank(message = "name should not be blank")
    private String name;

    @Size(max = 200, message = "the description should be no more than 200 characters")
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive(message = "the duration should be positive")
    private Integer duration;

}
