# ITLab-DocsGen

## Service for generate xls file

## Requirements
* jdk 8 or higher
* spring boot
* loombok

## Configuration

Example configuration file or environment variables

```java
Url = "url dev rtu lab"
Key = "Key Autorization"
Header = "Header Autarization"
IssuerUrl = "identity server url"
springdoc.api-docs.path=/api/docs-gen/api-docs.yaml
springdoc.swagger-ui.path=/api/docs-gen/swagger-ui.html
```

## Build

You can use gradlew or gradle on your system
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