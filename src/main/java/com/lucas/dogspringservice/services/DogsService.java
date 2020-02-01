package com.lucas.dogspringservice.services;

import com.lucas.dogspringservice.entity.Dog;
import com.lucas.dogspringservice.repository.DogRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component // Defines that this class is a spring bean
public class DogsService {

    final DogRepository dogRepository;

    public DogsService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public Dog getDogById(long id) {
        System.out.println("id: " + id);
        Optional<Dog> optionalDog = dogRepository.findById(id);
        System.out.println(optionalDog.isPresent()
                ? optionalDog.get().getName()
                : "no dog found");

        return optionalDog.orElse(null);
    }

    public List<Dog> getAllDogs() {
        System.out.println("Getting all dogs");
        List<Dog> allDogs = dogRepository.findAll();
        System.out.println(!allDogs.isEmpty()
                ? "Amount of dogs found: " + allDogs.size()
                : "No dogs found");

        return allDogs;
    }

    public Dog createDog(String name, String color) {
        System.out.println("Creating a dog for name: " + name + " and color: " + color);
        Dog dogCreated = dogRepository.save(new Dog(name, color));
        System.out.println("Dog created: " + dogCreated.getName() + " color: " + dogCreated.getColor());

        return dogCreated;
    }
}