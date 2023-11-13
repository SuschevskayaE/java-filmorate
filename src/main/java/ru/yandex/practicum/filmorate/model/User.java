package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Slf4j
public class User extends BaseUnit {

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^\\S+$")
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    private Set<Long> idFriends;
}
