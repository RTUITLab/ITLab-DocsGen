FROM gradle:6.5.1-jdk8 as build

USER root


COPY --chown=gradle:gradle . /ginfin/ITLab-DocsGen/src
WORKDIR /ginfin/ITLab-DocsGen/src
RUN gradle build --stacktrace

FROM adoptopenjdk:8-jre-hotspot
EXPOSE 8080

RUN ln -s /usr/lib/libfontconfig.so.1 /usr/lib/libfontconfig.so && \
    ln -s /lib/libuuid.so.1 /usr/lib/libuuid.so.1 && \
    ln -s /lib/libc.musl-x86_64.so.1 /usr/lib/libc.musl-x86_64.so.1
ENV LD_LIBRARY_PATH /usr/lib
COPY --from=build /ginfin/ITLab-DocsGen/src/build/libs/*.jar app.jar
CMD java -jar /app.jar