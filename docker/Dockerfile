# Build angular application
FROM node:20.17.0 as angular
WORKDIR /app
COPY frontend/package*.json .
RUN npm install
COPY frontend .
RUN npm run build

# Package the application of Spring Boot with Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY ../backend/pom.xml ./pom.xml
COPY ../backend/src ./src
RUN mvn clean package -DskipTests -Pproduction

#Create docker image with compiled project
FROM eclipse-temurin:21-jdk
WORKDIR /app
# Copy the generated JAR file from the build stage
COPY --from=build /app/target/*.jar .
EXPOSE 443
CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]