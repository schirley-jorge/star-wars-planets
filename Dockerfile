FROM openjdk:8-jdk-alpine

RUN apk update && apk add maven
RUN mkdir app
COPY . /app
WORKDIR /app
RUN mvn install
RUN cp /app/target/star-wars-planets*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]