# syntax=docker/dockerfile:1
# Stage 1 — build
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -B dependency:go-offline
COPY src/ src/
RUN ./mvnw -q -B clean package -DskipTests

# Stage 2 — runtime
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN chown 10001:10001 app.jar
USER 10001:10001
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]