CREATE SEQUENCE IF NOT EXISTS person_sequence MINVALUE 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS persons
(
    id          BIGINT          DEFAULT nextval('person_sequence') PRIMARY KEY,
    name        VARCHAR(50)     NOT NULL,
    gender      VARCHAR(10),
    birth_date  DATE            NOT NULL
);

CREATE TABLE IF NOT EXISTS interests
(
    person_id   BIGINT NOT NULL,
    interest    VARCHAR(30) NOT NULL,
    primary key (person_id, interest)
);
