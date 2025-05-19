# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---- Runtime Stage ----
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy built jar from the build stage
COPY --from=build /app/target/liquibase-app-1.0-SNAPSHOT.jar app.jar

# Add Liquibase changelog files
COPY src/main/java/resources /app/resources

# Run the app
ENTRYPOINT ["sh", "-c", "java -cp app.jar:resources com.example.liquibase.LiquibaseRunner"]
