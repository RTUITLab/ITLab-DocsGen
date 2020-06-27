FROM gradle:4.10.0-jdk8-alpine as build

USER root

RUN   apk update \
  &&   apk add ca-certificates wget \
  &&   update-ca-certificates

COPY --chown=gradle:gradle . /ginfin/ITLab-DocsGen/src
WORKDIR /ginfin/ITLab-DocsGen/src
RUN gradle build --stacktrace

FROM adoptopenjdk/openjdk8-openj9:jdk8u202-b08_openj9-0.12.1-alpine
EXPOSE 8081
RUN apk add --no-cache fontconfig
RUN ln -s /usr/lib/libfontconfig.so.1 /usr/lib/libfontconfig.so && \
    ln -s /lib/libuuid.so.1 /usr/lib/libuuid.so.1 && \
    ln -s /lib/libc.musl-x86_64.so.1 /usr/lib/libc.musl-x86_64.so.1
ENV LD_LIBRARY_PATH /usr/lib
COPY --from=build /ginfin/ITLab-DocsGen/src/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]