# `blogy`

Create a simple REST API to manage post's comments

# Settings

1. Create a `MySQL` database :
```
create database blogy
```

2. Edit `application.properties` in `src/main/resources` so your configuration meets project requirements
```
server.port=8081
spring.datasource.url = jdbc:mysql://localhost:3306/blogy?serverTimezone=GMT&useLegacyDatetimeCode=false
spring.datasource.username = root
#uncomment to set password
##spring.datasource.password=
```
# Run with Maven
```
mvn clean spring-boot:run
```

