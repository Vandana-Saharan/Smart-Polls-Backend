# ---- Build stage ----
    FROM maven:3.9.8-eclipse-temurin-17 AS build
    WORKDIR /app
    
    # Copy pom first for dependency caching
    COPY pom.xml .
    RUN mvn -q -e -DskipTests dependency:go-offline
    
    # Copy source and build
    COPY src ./src
    RUN mvn -q -DskipTests package
    
    # ---- Run stage ----
    FROM eclipse-temurin:17-jre
    WORKDIR /app
    
    # Copy the built jar
    COPY --from=build /app/target/*.jar app.jar
    
    # Render will set PORT env var; Spring should use it
    EXPOSE 8080
    ENTRYPOINT ["java","-jar","app.jar"]