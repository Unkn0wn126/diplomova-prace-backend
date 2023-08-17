# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy as base

WORKDIR /app

RUN apt-get update && apt-get install -y dos2unix

COPY .mvn/ .mvn
COPY mvnw ./mvnw
COPY pom.xml ./pom.xml

RUN dos2unix ./mvnw && apt-get --purge remove -y dos2unix && rm -rf /var/lib/apt/lists/*

RUN ./mvnw dependency:resolve

COPY src ./src

FROM base as development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=default", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base as build
RUN ./mvnw package

FROM eclipse-temurin:17-jdk-jammy as production
EXPOSE 8080
COPY --from=build /app/target/artificial-generation-backend-*.jar /artificial-generation-backend.jar
CMD ["java", "-Djava.security.edg=file:/dev/ ./urandom", "-jar", "/artificial-generation-backend.jar"]