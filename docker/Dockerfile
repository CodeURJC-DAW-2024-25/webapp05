# Build angular application
FROM node:20.17.0 as angular
WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend/src/assets/ ./src/assets/
COPY frontend/ .
RUN npx ng build --configuration production
#RUN npm run build -- --configuration production --base-href="/spa/"
# Package the application of Spring Boot with Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /gymdb
COPY ../backend/pom.xml ./pom.xml
RUN mvn dependency:go-offline
COPY ../backend/src ./src

COPY --from=angular /app/dist/webapp05 ./src/main/resources/static/spa
RUN mvn clean package -DskipTests -Pproduction



#Create docker image with compiled project
FROM eclipse-temurin:21-jdk
WORKDIR /app
# Copy the generated JAR file from the build stage
COPY --from=build /gymdb/target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 443
CMD ["java", "-jar", "app.jar"]