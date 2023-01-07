# java-filmorate
Приложение для работы с фильмами и оценками пользователей.

Схема базы данных:
![](diagramDB.png)

Примеры запросов:

1) Выборка данных из таблицы Users
   
SELECT u.user_id, u.email, u.login, u.name, u.birthday

FROM users AS u;


2) Добавление информации о пользователе

INSERT INTO users (email, login, name, birthday) 

VALUES (user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));


3) Обновление количества лайков

UPDATE films SET rate = rate + 1 WHERE film_id = id;


4) Удаление записей из таблицы Filmgenre

DELETE FROM filmgenre WHERE film_id = id;