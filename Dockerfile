FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM payara/server-full:6.2024.6
COPY --from=build /app/target/translator-api.war $DEPLOY_DIR
EXPOSE 8080