FROM maven:3.6.3-openjdk-14-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -f pom.xml clean package

FROM openjdk:latest
COPY --from=build /workspace/target/Trading-Bot.jar Trading-Bot.jar
USER 1001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Trading-Bot.jar"]