## Common errors

- Class `GitProperties` not found / could not be injected
    - Use `mvn clean compile`. This will generate the missing `git.properties` file in the `target` folder
- No main class found when running `mvn spring-boot:run`
    - Could be several reasons:
        - The `pom.xml` is not correct: The `main` class is not in the `<mainClass>` section or wrong
        - The `main` class is not in the `src/main/java` folder
        - The `main` class is not a `public static void main(String[] args)` method
