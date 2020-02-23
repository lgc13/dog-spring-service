package com.lucas.dogspringservice.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.lucas.dogspringservice.entity.Dog;
import com.lucas.dogspringservice.repository.DogRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Nested
    class createDog {

        @Test
        void whenSaveIsSuccessful_returnsDog() {
            String name = "some name";
            String color = "a color";
            Dog dog = new Dog(name, color);
            when(dogRepository.save(any(Dog.class))).thenReturn(dog);

            Dog result = dogsService.createDog(name, color);
            assertThat(result).isEqualTo(dog);
        }
    }
}
