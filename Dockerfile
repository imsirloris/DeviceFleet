FROM azul/zulu-openjdk:21

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle ./gradle
COPY src ./src

RUN chmod +x ./gradlew
RUN ./gradlew build
RUN JAR_NAME=$(cat jar-name.txt)

CMD ["sh", "-c", "java -jar build/libs/$(cat jar-name.txt)"]