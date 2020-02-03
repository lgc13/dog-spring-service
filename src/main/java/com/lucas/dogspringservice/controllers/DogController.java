package com.lucas.dogspringservice.controllers;

import com.lucas.dogspringservice.entity.Dog;
import com.lucas.dogspringservice.services.DogsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@RestController // Defines that this class is a spring bean
@RequestMapping("dog")
public class DogController {

    private final DogsService dogsService;

    @Autowired
    public DogController(DogsService dogsService) {
        this.dogsService = dogsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDogById(@PathVariable long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");

        return ResponseEntity.ok()
                .headers(headers)
                .body(dogsService.getDogById(id));
    }

    @GetMapping("/all")
    public List<Dog> getAllDogs() {
        return dogsService.getAllDogs();
    }

    @GetMapping("/create/{name}")
    public Dog createDog(@PathVariable String name, @RequestParam String color) {

        return dogsService.createDog(name, color);
    }



}