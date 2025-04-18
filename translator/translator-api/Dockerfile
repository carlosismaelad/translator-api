FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y openjdk-21-jdk maven
WORKDIR /app
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:21-jdk-slim

COPY --from=build /target/translatorapi-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/credentials.json credentials.json

ENV GOOGLE_CREDENTIALS_PATH=credentials.json

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]