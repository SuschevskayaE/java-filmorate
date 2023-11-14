# java-filmorate

####Привет!
Представляю вам схему базы данных учебного проекта:

![alt text](ВatabaseSchema.png)

**User** – содержит информацию о пользователях.\
**Film** – содержит информацию о фильмах.\
**Users_relations** – содержит информацию о связях “дружба” пользователей. Поле relation_accepted указывает статус связи: подтвержденная или неподтверждённая.\
**Likes** – содержит информацию о лайках фильмов. Один пользователь может поставить лайк одному фильму, один фильм может получить несколько лайков от пользователей.\
**Genre** – содержит информацию о жанрах фильмов.\
**Film_genres** – содержит информацию о жанрах фильма. Один фильм может содержать несколько жанров.\
**Mpa_rating** – содержит информацию о рейтинге Ассоциации кинокомпаний.

### Основные запросы

####User Storage

Get all:
```roomsql
SELECT * FROM user;
```

Get by id:
```roomsql
SELECT *
FROM user
WHERE id = 1;
```

Create:
```roomsql
INSERT INTO user(email, login, name, birthday)
VALUES('email@mail,ru', 'login', 'name','2000-01-01');
```

Update:
```roomsql
UPDATE user
SET
email ='login@mail.ru', 
Login ='login@mail.ru', 
name ='name', 
birthday = '2000-01-01'
WHERE id = 1;
```

####Film Storage

Get all:
```roomsql
SELECT f.name,
f.description,
f.releaseDate,
f.duration,
mr.rating
FROM film f
LEFT JOIN mpa_rating mr on f.mpa_id = mr.id;
```

Get by id:
```roomsql
SELECT f.name,
f.description,
f.releaseDate,
f.duration,
mr.rating
FROM film f
LEFT JOIN mpa_rating mr on f.mpa_id = mr.id
WHERE id = 1;
```

Create:
```roomsql
INSERT INTO film (name, description, releaseDate, duration, mpa_id)
VALUES('name', 'description', '2023-01-01', 120, 1);
```

Update:
```roomsql
UPDATE film
SET
name = 'name',
description = 'Description',
releaseDate = '2023-01-01',
duration = 100,
rating = 1
where id = 1
```