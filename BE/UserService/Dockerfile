# 1) BUILD STAGE: ima i JDK i Maven
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Kopiramo prvo pom da Maven povuče dependencye (cache-friendly)
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Sad kopiramo source kod
COPY src ./src

# Build JAR
RUN mvn -q clean package -DskipTests

# 2) RUNTIME STAGE: samo JRE, lagana slika
FROM eclipse-temurin:21-jre
WORKDIR /app

# Kopiramo build-ovan jar iz builder stage-a
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
