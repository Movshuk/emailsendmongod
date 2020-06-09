# Сервис отправки почтовых сообщений
**rest api, почтовая служба smtp.yandex.ru***

## Функционал:
1. Создание и хранение сообщения
2. Удаление и изменение сообщения
3. Добавление к сообщению файлов и адресатов
4. Отправка сообщения
5. Логгирование событий
6. Обработка ошибки отправки (статут сообщения ERROR)

## Stack:
* Java 8+
* Maven 3.6.3
* Spring Boot 2.2.6.RELEASE
* Swagger 2.9.2
* Mongo DB 3.6.8
* JUnit 5.0.0-ALPHA

## Инсталляция
    mvn -Dmaven.test.skip=true install

## Пуск через терминал
вместо {EMAIL_NAME} и {PASSWORD} передать значения почтового ящика для yandex почты.

    java -jar email-0.0.1-SNAPSHOT.jar \this-is-a-non-option-arg \
    --spring.mail.username={EMAIL_NAME} \
    --spring.mail.password={PASSWORD}

## Пуск через Intellij IDEA параметры VM options
spring.mail.username и spring.mail.password определены в application.properties:

    -DUSERNAME={EMAIL_NAME}
    -DPASSWORD={PASSWORD}

## Документация RESTful Swagger:
    http://localhost:8080/swagger-ui.html#/
 
