package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private Long id;

    @NotBlank(message = "email should not be blank")
    @Email(message = "Email address has invalid format: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @NotBlank(message = "login should not be blank")
    @Pattern(message = "Bad formed person name: ${validatedValue}",
            regexp = "^.[^\\s]+$")
    private String login;

    private String name;

    @Past(message = "date of birth cannot be in the future")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

}
