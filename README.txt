

Esta api fue hecha en con java 1.8 y Spring boot en Visual Studio Code, asi que si se requeire correr en desde visual studio code, es necesario instalar las extenciones
	-Debugger for Java
	-Extension Pack for java
	-Language Support for java(TM) by Red Hat
	-Lombok Annotetions Support for VS Code
	-Maven for java-Project Manager for Java
	-Red Hat Commons
	-Spring Boot Dashboard
	-Spring Boot Extension Pack
	-Spring Boot Tools
	-Spring Initializr Java Support
	-Test Runner for java
	-XML (Red Hat)

El api se conecta a una base de datos MySQL, para descargar el manejador de bd se puede descargar desde la siguiente liga: https://dev.mysql.com/downloads/mysql/
Una vez instalado y configurado MySQL es necesario ejecutar los siguientes scripts:

create database test;
use test;

create table account(
id int primary key auto_increment,
name varchar(255),
mail varchar(255),
account varchar(255),
balance float
);

create table movement(
id int primary key auto_increment,
movement_code varchar(255),
description varchar(255),
amount float,
account varchar(11),
create_date date
);

Configurar application.prooerties
Poner los datos correspondientes de la base de datos a la que se requiere conectar

spring.datasource.url=jdbc:mysql://localhost:3306/myDataBase?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&zeroDateTimeBehavior=convertToNull
spring.datasource.username=
spring.datasource.password=

Una vez configurado puede correr el proyecto y acceder a el desde la url: http://localhost:8484/test-api
Por si se requiere cambiar el puerto, se encuentra configurado en el archivo .vscode\launch.json
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug (Launch)-App<caem-api>",
            "request": "launch",
            "mainClass": "com.alfredo.test.apirest.App",
            "projectName": "caem-api",
            "vmArgs": "-Dspring.profiles.active=local -Dserver.port=8484"
        }
    ]
}


CONSUMIR API
En el proyecto se en cuentra un archivo llamado thunder-collection_Consulting Test A.json, en el se encuentran las peticiones para consumir el API

Para poder abrir este archivo es necesario instalar la extencion Thunder Client


Swagger
Para ver los endpoind desde swagger los puedes ver desde la url
http://localhost:8484/test-api/swagger-ui.html