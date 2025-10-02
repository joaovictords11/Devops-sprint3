FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-focal

WORKDIR /app

RUN groupadd -r appgroup && useradd --no-log-init -r -g appgroup appuser

COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

EXPOSE 8080

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]