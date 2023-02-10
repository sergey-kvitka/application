FROM eclipse-temurin:17-jdk-alpine
ADD target/*.jar application.jar
ENTRYPOINT ["java", "-jar", "/application.jar"]
