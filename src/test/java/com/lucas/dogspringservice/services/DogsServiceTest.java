package com.lucas.dogspringservice.services;

import com.lucas.dogspringservice.entity.Dog;
import com.lucas.dogspringservice.repository.DogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DogsServiceTest {

    @Mock
    private DogRepository dogRepository;

    @InjectMocks
    private DogsService dogsService;

    @Nested
    class getDogById {
        @Test
        public void whenNoDogInRepository_returnsNull() {
            when(dogRepository.findById((long) 1)).thenReturn(Optional.empty());
            Dog result = dogsService.getDogById(1);
            assertThat(result).isNull();
        }

        @Test
        public void whenDogFoundInRepo_returnsDog() {
            Dog dog = new Dog("Lucas", "red");
            when(dogRepository.findById((long) 1)).thenReturn(Optional.of(dog));
            Dog result = dogsService.getDogById(1);

            assertThat(result).isEqualTo(dog);
        }
    }

    @Nested
    class getAllDogs {
        @Test
        void whenRepositoryReturnsAList_returnsTheList() {
            Dog dog1 = new Dog("dogName", "someColor");
            Dog dog2 = new Dog("another name", "another color");
            List<Dog> dogList = List.of(dog1, dog2);
            when(dogRepository.findAll()).thenReturn(dogList);
            List<Dog> result = dogsService.getAllDogs();

            assertThat(result).isEqualTo(dogList);
        }
    }
}