package com.lucas.dogspringservice.controllers;

import com.lucas.dogspringservice.entity.Dog;
import com.lucas.dogspringservice.services.DogsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController // Defines that this class is a spring bean
@RequestMapping("dogs")
@Slf4j
@RequiredArgsConstructor
public class DogController {
    private final DogsService dogsService;

    @GetMapping("")
    public List<Dog> getAllDogs() {
        List<Dog> dogList = dogsService.getAllDogs();
        log.info("Returning {} dog(s)", dogList.size());
        return dogList;
    }

    @PostMapping("")
    public Dog createDog(@RequestParam String name, @RequestParam String color) {
        log.info("Creating dog with name: {} and color: {}", name, color);
        return dogsService.createDog(name, color);
    }

    @GetMapping("/{id}")
    public Dog getDogById(@PathVariable long id) {
        log.info("Getting dog by id: {}", id);
        return dogsService.getDogById(id);
    }
}
