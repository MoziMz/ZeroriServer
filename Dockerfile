FROM eclipse-temurin:17.0.13_11-jdk
ARG JAR_FILE=/build/libs/MoziServer-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar","/app.jar"]
