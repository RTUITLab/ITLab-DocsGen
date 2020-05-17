FROM gradle:4.10.0-jdk8-alpine as build

USER root

RUN   apk update \
  &&   apk add ca-certificates wget \
  &&   update-ca-certificates

COPY --chown=gradle:gradle . /ginfin/ITLab-DocsGen/src
WORKDIR /ginfin/ITLab-DocsGen/src
RUN gradle build --stacktrace

FROM openjdk:8-jdk-alpine
EXPOSE 8081
COPY --from=build /ginfin/ITLab-DocsGen/src/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
