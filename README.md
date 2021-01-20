# ITLab-DocsGen
## Service for generate xls file
## Requirements
* jdk 8 or higher
* spring boot
* loombok 
## Configuration
 Example configuration file
   
    ```java
    EventsBaseAddress = " service ITLab-Back base address"
    Key = "Key authorization"
    Header = "Header authorization"
    IssuerUrl = "identity server url"
    springdoc.api-docs.path=/api/Docs-Gen/api-docs.yaml
    springdoc.swagger-ui.path=/api/Docs-Gen/swagger-ui.html