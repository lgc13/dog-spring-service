package com.lucas.dogspringservice.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.dogspringservice.entity.Dog;
import com.lucas.dogspringservice.services.DogsService;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DogController.class)
class DogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DogsService dogsService;

    @Nested
    class getAllDogs {

        @Test
        public void whenDogsFound_returnsListOfDogs() throws Exception {
            Dog dog = new Dog("Sasha", "blue");
            List<Dog> dogList = List.of(dog);
            when(dogsService.getAllDogs()).thenReturn(dogList);

            mockMvc
                .perform(get("/dogs"))
                .andExpect(content().string(objectMapper.writeValueAsString(dogList)));
        }
    }

    @Nested
    class createDog {

        @Test
        public void whenServiceCreatesTheDog_returnsDogCreated() throws Exception {
            String name = "Sasha";
            String color = "blue";
            Dog dog = new Dog("Sasha", "blue");

            when(dogsService.createDog(name, color)).thenReturn(dog);

            mockMvc
                .perform(post("/dogs").param("name", "Sasha").param("color", "blue"))
                .andExpect(content().string(objectMapper.writeValueAsString(dog)));
        }
    }

    @Nested
    class getDogById {

        @Test
        public void whenDogIsFoundByIdProvided_returnsDog() throws Exception {
            Dog dog = new Dog("Sasha", "blue");
            when(dogsService.getDogById(1)).thenReturn(dog);

            mockMvc
                .perform(get("/dogs/1"))
                .andExpect(content().string(objectMapper.writeValueAsString(dog)));
        }
    }
}
