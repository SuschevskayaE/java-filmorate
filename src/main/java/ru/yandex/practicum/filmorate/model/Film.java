package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Slf4j
public class Film {

    private Integer id;

    @NotBlank
    private String name;

    @Size(min =1, max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private Integer duration;
}
