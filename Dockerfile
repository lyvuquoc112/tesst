# ---- Stage 1: Build ----
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml và tải dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code và build
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy file jar từ stage build
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
