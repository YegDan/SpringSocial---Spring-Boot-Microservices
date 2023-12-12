
FROM gradle:8-jdk17-alpine AS builder



COPY --chown=gradle:gradle . /home/gradle/src


WORKDIR /home/gradle/src


RUN gradle build -x test


FROM openjdk:17-alpine

RUN mkdir /app


COPY --from=builder /home/gradle/src/build/libs/*.jar /app/comment-service.jar


ENV MONGO_DB_USERNAME=admin \
    MONGO_DB_PWD=password


EXPOSE 8087


ENTRYPOINT ["java", "-jar", "/app/comment-service.jar"]