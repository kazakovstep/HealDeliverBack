FROM openjdk:17

WORKDIR /app

COPY build/libs/eco_market-0.0.1-SNAPSHOT.jar my-spring-app.jar

EXPOSE 8080

CMD ["java", "-jar", "my-spring-app.jar"]