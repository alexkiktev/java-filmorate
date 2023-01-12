--DELETE FROM genres;
--DELETE FROM filmgenre;
--DELETE FROM mpa;
DELETE FROM friends;
DELETE FROM filmgenre;
DELETE FROM userlikes;
DELETE FROM films;
ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1;
DELETE FROM users;
ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;

MERGE INTO mpa (mpa_id, name)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

MERGE INTO genres (genre_id, name)
    VALUES (1,'Комедия'),
           (2,'Драма'),
           (3,'Мультфильм'),
           (4,'Триллер'),
           (5,'Документальный'),
           (6,'Боевик');