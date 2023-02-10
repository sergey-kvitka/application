FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/*.jar
ADD JAR_FILE application.jar
ENTRYPOINT ["java", "-jar", "/application.jar"]