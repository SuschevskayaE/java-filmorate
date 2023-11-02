package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Data
@Slf4j
public class User {

    private Integer id;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^\\S+$")
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    private Set<Integer> idFriends;
}
