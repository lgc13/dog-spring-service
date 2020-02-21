package com.lucas.dogspringservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.dogspringservice.entity.Dog;
import com.lucas.dogspringservice.services.DogsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(DogController.class)
class DogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    DogsService dogsService;

    @Test
    public void getDogById_whenDogIsFoundByIdProvided_returnsDog() throws Exception {
        Dog dog = new Dog("Sasha", "blue");
        when(dogsService.getDogById(1)).thenReturn(dog);

        mockMvc.perform(get("/dog/1"))
                .andExpect(content().string(objectMapper.writeValueAsString(dog)));
    }


}