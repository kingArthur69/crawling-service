FROM amazoncorretto:17-alpine-jdk as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

#Experimental feature to cache maven dependecy
#RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests
#RUN dos2unix mvnw && chmod +x mvnw

RUN ./mvnw install -DskipTests
# RUN sed -i 's/\r$//' mvnw && /bin/sh mvnw install -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM amazoncorretto:17-alpine-jdk
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8080

ENV JAVA_OPTS ""
#ENV JAVA_OPTS "-Dspring.data.mongodb.host=host.docker.internal"

ENTRYPOINT java $JAVA_OPTS -cp "app:app/lib/*" com.amihaliov.crawlingservice.CrawlingServiceApplication
