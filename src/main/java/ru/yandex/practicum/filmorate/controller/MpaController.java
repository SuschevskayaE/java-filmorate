package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public List<MpaRating> getAll() {
        final List<MpaRating> mpaRatings = mpaService.getAll();
        return mpaRatings;

    }

    @GetMapping("/{id}")
    public MpaRating get(@PathVariable long id) {
        final MpaRating mpaRating = mpaService.get(id);
        return mpaRating;
    }
}
