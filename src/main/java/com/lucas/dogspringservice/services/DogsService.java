package com.lucas.dogspringservice.services;

import com.lucas.dogspringservice.entity.Dog;
import com.lucas.dogspringservice.repository.DogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component // Defines that this class is a spring bean
@Slf4j
public class DogsService {

    final DogRepository dogRepository;

    public DogsService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public Dog getDogById(long id) {
        Optional<Dog> optionalDog = dogRepository.findById(id);
        if (optionalDog.isEmpty()) {
            log.info("No dog was found by id: {}", id);
        }
        return optionalDog.orElse(null);
    }

    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }

    public Dog createDog(String name, String color) {
        return dogRepository.save(new Dog(name, color));
    }
}