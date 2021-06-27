# Marvel Characters API

This is a project written in Java 11 using SpringBoot to serve an API which allows users
to view limited information about Marvel characters.

The swagger is under path ```marvel-characters-api/src/main/resources/marvel-characters-api.yaml```.

## Environment Variables
The application expects two environment variables in order to contact with the Marvel API.

``PRIVATE_KEY``: The private key obtained from https://developer.marvel.com/account

``PUBLIC_KEY``: The public key obtained from https://developer.marvel.com/account

## How to run

It's a spring boot app and can be run from within your IDE or as a JAR.

1. Build the application ```mvn clean install```
2. Ensure the JAR has been created `C:\Users\user\.m2\repository\com\yapily\marvel-characters-service\0.0.1-SNAPSHOT`

### Using a git bash shell
3. Load the Environment variables into your shell
``source PRIVATE_KEY='' && source PUBLIC_KEY=""``
   
4, Run the jar
   ``java -jar C:\Users\user\.m2\repository\com\yapily\marvel-characters-service\0.0.1-SNAPSHOT\marvel-characters-service-0.0.1-SNAPSHOT.jar``

### Through IntelliJ
Create a Run Configuration pointing to the generated JAR and importing the environment variables.

### Without environment variables

Override the spring configuration through the command line.

``java -jar C:\Users\user\.m2\repository\com\yapily\marvel-characters-service\0.0.1-SNAPSHOT\marvel-characters-service-0.0.1-SNAPSHOT.jar --key.private="" 
--key.public=""
``

## Requirements to run
- Java 11
- SpringBoot
- Maven