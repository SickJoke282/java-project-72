FROM eclipse-temurin:20-jdk

WORKDIR ./app

COPY ./app/gradle ./app/gradle
COPY ./app/build.gradle.kts .
COPY ./app/settings.gradle.kts .
COPY ./app/gradlew .

RUN ./app/gradlew --no-daemon dependencies

COPY ./app/src ./app/src
COPY ./app/config ./app/config

RUN ./app/gradlew --no-daemon build

ENV JAVA_OPTS "-Xmx512M -Xms512M"
EXPOSE 7070

CMD java -jar build/libs/HexletJavalin-1.0-SNAPSHOT-all.jar