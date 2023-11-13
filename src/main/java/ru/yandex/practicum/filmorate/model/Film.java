package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Slf4j
public class Film extends BaseUnit {

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

    private Set<Long> likes;
}
