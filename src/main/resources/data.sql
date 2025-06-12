MERGE INTO persons(id, name, gender, birth_date)
VALUES (-1,'Will Robinson', 'MALE', '2030-01-01');

MERGE INTO interests(person_id, interest)
VALUES (-1, 'robots'),(-1, 'geology'),(-1, 'electronics'),(-1, 'space');