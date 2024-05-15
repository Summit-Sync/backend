FROM eclipse-temurin:21 as build

WORKDIR /summit-sync

COPY . /summit-sync

RUN ./gradlew clean build -x test

FROM eclipse-temurin:21

LABEL org.opencontainers.image.source=https://github.com/Summit-Sync/backend

WORKDIR /summit-sync
COPY --from=build /summit-sync/build/libs/summit-sync-0.0.1-SNAPSHOT.jar .
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "summit-sync-0.0.1-SNAPSHOT.jar"]
