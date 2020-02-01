# Spring Boot Example

## Links, videos, and resources:

Getting Started with Spring Boot: https://www.youtube.com/watch?v=sbPSjI4tt10

Maven video: https://www.youtube.com/watch?v=KNGQ9JBQWhQ

JPA: https://www.javaworld.com/article/3379043/what-is-jpa-introduction-to-the-java-persistence-api.html
skim this: https://www.baeldung.com/jpa-entities

Repositories: http://blog.sapiensworks.com/post/2014/06/02/The-Repository-Pattern-For-Dummies.aspx

## Create Project:

For this example, let's say I'm creating a `dog-spring-service` on the Desktop (aka on ~/Desktop/dog-spring-service/)

1. Download project start up

- Go here to download your zip file with any start up dependencies you want:

   https://start.spring.io/

 - Unzip folder

 2. Start it:

 ```sh
 cd ~/Desktop/dog-spring-service # Go to root directoy of your project
```

 - For Maven Projects:

 ```sh
 mvn spring-boot:run # For Maven Projects
 ```

 - For Gradle Projects:

 ```sh
 gradle bootRun # For Gradle Projects

 # If you don't have Gradle installed:
 brew install gradle

 # If you need to build:
 gradle build
 ```

 ## Simple Rest API

1. Create controller

 ```sh
 # 1. Creating controllers folder:
 mkdir ~/Desktop/dog-spring-service/src/main/java/com/lucas/dogspringservice/controllers

 # 2. Create controller (ex: DogsController.java)
 cd ~/Desktop/dog-spring-service/src/main/java/com/lucas/dogspringservice/controllers
 touch DogsController.java
 ```

- Make simple RestController:

 ```java
 // DogsController.java
package com.lucas.dogspringservice.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("dogs")
public class DogController {

    @GetMapping("/")
    public String getAllDogs() {
        System.out.println("Return all dogs here!");
        return "I am a Dog! (string)...";
    }
}
 ```

 - If the spring web framework is not installed, you can add it:

 ```sh
 # Gradle:

 # Go to build.gradle and change:
 implementation 'org.springframework.boot:spring-boot-starter' # from

 implementation 'org.springframework.boot:spring-boot-starter-web' # to
 ```

 2. Call API

 From a browser or Postman, call:

 `http://localhost:8080/dogs/`


## Database integration

1. Create your database

(I used psql to create a public db)

```shell script
# in psql
CREATE DATABASE dogsdb;

\c dogsdb # connects to db
```

2. Connect to database

- you probably have a `src/main/resources/application.properties` file. 
   Delete that one and create a `application.yml` file instead
- in your `src/main/resources/application.yml`, add the following:

```yml
### Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring:
  datasource:
    driverClassName: org.postgresql.Driver
  jpa:
    properties.hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect # The SQL dialect makes Hibernate generate better SQL for the chosen database
    hibernate.ddl-auto: update # Hibernate ddl auto (create, create-drop, validate, update)
  
```

- you can also create a `application-local.yml` so you can use it for a local profile (and not push on git)

```yml
### Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/dogsdb # any your db name here in the end. in our case, dogsdb
    username: myUserNameGoesHere
    password: somePassWord # if you have one
    driverClassName: org.postgresql.Driver
  jpa:
    properties.hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect # The SQL dialect makes Hibernate generate better SQL for the chosen database
    hibernate.ddl-auto: update # Hibernate ddl auto (create, create-drop, validate, update)
```

3. Create an entity

- An @Entity is what is used for each table so that `Hibernate` can handle it

Here's an entity example:

```java
package com.lucas.dogspringservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

@Entity
@Table(name = "dog")
public class Dog {
    @Id
    @GeneratedValue // no need for column here
    private long id;

    @Column(name = "name")
    private String name;
}
```

4. Import needed dependencies

```shell script
# buid.gradle file

# in order to use Hibernate, we need to import a jpa dependency:
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

# postgres driver needed:
implementation 'org.postgresql:postgresql'
```

Now when you run your project, it'll create that table every time it starts.
 
 
## Liquibase

Example: https://www.roytuts.com/spring-boot-liquibase-gradle-example/

1. Add needed dependency

```sh
# gradle
implementation 'org.liquibase:liquibase-core:3.8.2'
```

2. Create changelog file

```sh
# in projRoot/src/main/resources/db

touch db.changelog-master.yaml # this can also be .xml, .json, or .sql formats
```

3. Add a change log into the file

```yaml
# example
databaseChangeLog:
  - changeSet:  # you could have multiple change Sets
      id: 1     # each changeSet needs its own id
      author: lucasc
      changes:
        - createTable:
            tableName: dogsdb
            columns:
              - column:
                  name: id
                  type: int
                  autoIncrement: true
                  constraints:
                    primaryKey: true
                    nullable: false
              - column:
                  name: first_name
                  type: varchar(50)
                  constraints:
                    nullable: false
              - column:
                  name: last_name
                  type: varchar(50)
                  constraints:
                    nullable: false
```

4. Add the classpath to your changelog file

```sh
# in src/main/resource/application.properties

#liquibase.change-log=classpath:db/db.changelog-master.yaml
spring.liquibase.changeLog=classpath:db/db.changelog-master.yaml
```
 
## Annotations:

### @Autowired

This guy had a good answer: https://stackoverflow.com/a/3153617/8238895

`Autowiring happens by placing an instance of one bean into the desired field in an instance of another bean. Both classes should be beans, i.e. they should be defined to live in the application context.`

Example:

```java
// DogsController.java
@RestController // Defines that this class is a spring bean
@RequestMapping("dogs")
public class DogsController {

    @Autowired // Tells the application context to inject an instance of DogsService here
    private DogsService dogsService;

    @GetMapping("/")
    public String getAllDogs() {
        // The DogsService is already injected and you can use it
        return dogsService.getAllDogs();
    }
}

// DogsService.java
import org.springframework.stereotype.Component;

@Component // Defines that this class is a spring bean
public class DogsService {
    public String getAllDogs() {
        return "Im in Dogs service";
    }
}
```

### @Entity

   - lets you use `@Entity`, `@Id`, `@GeneratedValue`

   import jpa dependency:
   
   ```xml
   <!-- Maven -->
   <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
   </dependency>
  ```
  
