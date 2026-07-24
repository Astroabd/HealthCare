##without maven build u need to build the jar file first and then copy it to the docker image manually
#FROM eclipse-temurin:17-jdk
#
##container information
#LABEL maintainer="Astro"
#LABEL description="This is a config server for spring cloud config"
#LABEL version="0.0.1"
#LABEL license="Apache License 2.0"
#WORKDIR /app
#COPY target/configserver-0.0.1-SNAPSHOT.jar  app.jar
#
#
##the best practice is to sepcifiy this in dockercompose.yml file
#EXPOSE 8888
#ENTRYPOINT ["java", "-jar", "app.jar"]

##Compile the jar file first
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
##Excuate the jar file in the final image
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]