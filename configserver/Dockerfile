# Stage 1: compile the Spring Boot application
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Download dependencies separately so Docker can cache them
COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline

# Copy and compile the source code
COPY src ./src
RUN mvn -B -ntp clean package -DskipTests


# Stage 2: run the compiled application
FROM eclipse-temurin:17-jre-jammy

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system spring \
    && useradd --system --gid spring --create-home spring \
    && mkdir -p /home/spring/.ssh \
    && chown -R spring:spring /home/spring

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

USER spring:spring

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "/app/app.jar"]