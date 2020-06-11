# Star Wars Planet API
API que contém dados dos planetas da franquia Star Wars.

## Pré requisitos
- Java 1.8 ou superior;
- Docker compose
- Maven

## Como rodar a aplicação
1. Clone o repositório a partir do github
```sh
git clone https://github.com/schirley-jorge/star-wars-planets.git
```
2. Inicialize o banco de dados
```sh
docker-compose up -d
```
3. Execute a aplicação
```sh
./mvnw spring-boot:run
```
Or
```sh
./bin/star-wars-planets-0.0.1-SNAPSHOT.jar
```
A aplicação poderá ser acessada a partir da url *http://localhost:8080/*

## Como rodar os testes
```sh
mvn test
```

## Tecnologias utilizadas
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MongoBD](https://docs.mongodb.com/manual/)
- [Redis](https://redis.io/documentation)
- [Swagger](https://swagger.io/tools/open-source/getting-started/)
- [Jacoco](https://www.eclemma.org/jacoco/)
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)

## Documentação da API
Acesse a especificação da API no Swagger no [link](http://localhost:8080/swagger-ui.html)

## Algumas considerações:
- A anotação **@Indexed(unique = true)** no nome do documento/entidade *Planet* não está funcionando, ou seja, o banco está permitindo salvar planetas com o mesmo nome, por essa razão foi necessário colocar verificação se o planeta já existe antes de salvar no MongoDB;
- O **Jacoco** não está gerando o relatório de coverage, existe um conflito com o atributo *argLine* do surefire **<argLine>-Xmx1024m -XX:MaxPermSize=256m ${argLine}</argLine>** e isso impede a geração do relatório de *code coverage* formatado.
