FROM eclipse-temurin:17-jdk-alpine
LABEL authors="synapsis"
ARG JAR_FILE=build/libs/*.jar
ENV DB_HOST localhost
ENV DB_PORT 5432
ENV DB_USERNAME user_watcher
ENV DB_PASSWORD password
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]