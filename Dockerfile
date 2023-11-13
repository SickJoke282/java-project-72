FROM eclipse-temurin:20-jdk

WORKDIR /app

COPY /app .

RUN cd app && gradle installDist

CMD ./build/install/app/bin/app