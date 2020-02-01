package com.lucas.dogspringservice.repository;

import com.lucas.dogspringservice.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

}
