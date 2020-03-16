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

```shell script
cd ~/Desktop/dog-spring-service # Go to root directoy of your project
```

 - For Maven Projects:

 ```shell script
 mvn spring-boot:run # For Maven Projects
 ```

 - For Gradle Projects:

 ```shell script
gradle bootRun # For Gradle Projects

# If you don't have Gradle installed:
brew install gradle

# If you need to build:
gradle build

# If you have issues, or if it can't find the `main` method, then clean it:
gradle clean

# Running with a profile:
gradle bootRun --args="--spring.profiles.active=local" # `local` is the profile here

# Run tests:
gradle test
# or 
./gradlew test
 ```

 ## Simple Rest API

1. Create controller

 ```shell script
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

    @GetMapping("")
    public String getAllDogs() {
        System.out.println("Return all dogs here!");
        return "I am a Dog! (string)...";
    }
}
 ```

 - If the spring web framework is not installed, you can add it:

 ```shell script
 # Gradle:

 # Go to build.gradle and change:
 implementation 'org.springframework.boot:spring-boot-starter' # from

 implementation 'org.springframework.boot:spring-boot-starter-web' # to
 ```

 2. Call API

 From a browser or Postman, call:

 `http://localhost:8080/dogs`


## Database integration

1. Create your database

(I used `psql` to create a public db)

```shell script
# in psql
CREATE DATABASE dogsdb;

\c dogsdb # connects to db
```

2. Connect to database

- you probably have a `src/main/resources/application.properties` file. 
   Delete that one and create a `application.yml` file instead
- in your `src/main/resources/application.yml`, add the following:

```yaml
### Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring:
  datasource:
    driverClassName: org.postgresql.Driver
  jpa:
    properties.hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect # The SQL dialect makes Hibernate generate better SQL for the chosen database
    hibernate.ddl-auto: update # Hibernate ddl auto (create, create-drop, validate, update)
  
```

- ~~you can also create a `application-local.yml` so you can use it for a local profile (and not push on git)~~

```yaml
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

- As per the [12-factor-app CONFIG rule](https://12factor.net/config), we should not have config (passwords/secrets) in files that are in your repo (or can possibly be committed)
- One way to accomplish that is by using env vars:

```yaml
server.port: ${PORT}

### Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring:
  datasource: # no url, username, or password
    driverClassName: org.postgresql.Driver
  jpa:
    properties.hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect # The SQL dialect makes Hibernate generate better SQL for the chosen database
    hibernate.ddl-auto: update # Hibernate ddl auto (create, create-drop, validate, update)
```
   
- Add a common configuration to build your db connection:
   - BasicDataSource is a dependency from apache. (more below)

```groovy
// add dependency to build.gradle
implementation 'org.apache.commons:commons-dbcp2:2.7.0'
```

```java
// DataSourceConfig.java

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Bean
    public BasicDataSource dataSource() throws URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }
}
```

```shell script
# Lastly, add any/all env variables needed to your run configuration (can be done through IntelliJ). In this case:

PORT: # some port number. ex: 8081
DATABASE_URL: # your url. ex: postgres://username:password@localhost:5432/databaseName
```   

3. Import needed dependencies

```shell script
# buid.gradle file

# in order to use Hibernate, we need to import a jpa dependency:
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

# postgres driver needed:
implementation 'org.postgresql:postgresql'
```

4. Create an entity

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

Now when you run your project, it'll create that table every time it starts.
 
 
## Liquibase

Example: https://www.roytuts.com/spring-boot-liquibase-gradle-example/

1. Add needed dependency

```shell script
# gradle
implementation 'org.liquibase:liquibase-core:3.8.2'
```

2. Create changelog file

```shell script
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

```shell script
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

    @GetMapping("")
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

## Lombok

1- Add the dependency:

```groovy
# build.gradle file
id "io.freefair.lombok" version "5.0.0-rc2"

# check latest version on their website: https://plugins.gradle.org/plugin/io.freefair.lombok
```

2- Add the annotation and import:

```java
import lombok.extern.slf4j.Slf4j; // import

@Slf4j // annotation
public class SomeClass{}
```

3- Log to your heart's content


```java
import lombok.extern.slf4j.Slf4j; // import

@Slf4j // annotation
public class SomeClass{
    public void someMethod(int id) {
        log.info("Getting dog by id: {}", id);
    }
}
```

### API Naming/Best Practices

Check out these links: [doc1](https://restfulapi.net/resource-naming/), [doc2](https://docs.microsoft.com/en-us/azure/architecture/best-practices/api-design)

```text
- use plural `nouns`, not verbs
    GOOD: /api/dogs
    BAD: /api/getDogs

- use HTTP method types for requests
    Getting a list of dogs:

    GOOD: GET /api/dogs - returns all dogs
    BAD: POST /api/dogs - should be used to ADD a dog

```

- `define operations by HTTP request:`

| Resource            | GET                                 | POST                              |  PUT                                          | DELETE                           |
| ------------------- | ------------------------------------| --------------------------------- | ----------------------------------------------| -------------------------------- |
| /customers          | Retrieve all customers              | Create a new customer             | Bulk update of customers                      | Remove all customers             |
| /customers/1        | Retrieve the details for customer 1 | Error                             | Update the details of customer 1 if it exists | Remove customer 1                |
| /customers/1/orders | Retrieve all orders for customer 1  | Create a new order for customer 1 | Bulk update of orders for customer 1          | Remove all orders for customer 1 |

#### Adding an Interceptor

```java
// RequestInterceptor.java
package com.lucas.dogspringservice.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Override
    public boolean preHandle(  // runs before every request
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        String xForwardedHostHeader = request.getHeader("x-forwarded-host");
        String xForwardedProtoHeader = request.getHeader("x-forwarded-proto");
        String fullUrlHeader = xForwardedProtoHeader + "://" + xForwardedHostHeader;

        log.info("gatewayUrl: {}", gatewayUrl);
        if (fullUrlHeader.equals(gatewayUrl)) {
            return true;
        } else {
            log.info("Unauthorized request from {}", fullUrlHeader);
            response.setStatus(401);  // if urls don't match, return a 401 Unauthorized
            return false;
        }
    }
}


// dont forget to use this in a config file. ex: CommonConfiguration.java
@Configuration
@RequiredArgsConstructor
public class CommonConfiguration implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;
    
    // ... others
}
```
  
