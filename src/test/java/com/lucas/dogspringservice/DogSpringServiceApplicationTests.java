package com.lucas.dogspringservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase // this replaces dataSource bean with embedded version
class DogSpringServiceApplicationTests {

	@Test
	void contextLoads() {
	}
}
