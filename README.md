# ITLab-DocsGen

## Service for generate xls file

## Requirements
* jdk 8 or higher
* spring boot
* loombok

## Configuration

Example configuration file or environment variables
```java
EventsBaseAddress = "service ITLab-Back base address"
Key = "Key authorization"
Header = "Header authorization"
IssuerUrl = "identity server url"
springdoc.api-docs.path=/api/Docs-Gen/api-docs.yaml
springdoc.swagger-ui.path=/api/Docs-Gen/swagger-ui.html
```

## Build

You can use `gradlew` or `gradle` on your system
```bash
./gradlew build
./gradlew copyLibToDeploy
```

## Build docker image

After [build](#build)
```
cd deploy
docker build -t imagename .
```
