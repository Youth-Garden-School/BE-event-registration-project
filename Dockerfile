FROM gradle:8.13-jdk21 AS build

WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
COPY src ./src

# Build the application
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

FROM amazoncorretto:21.0.4

WORKDIR /app

# Copy JAR file from build stage
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
