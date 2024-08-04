-- liquibase formatted sql

-- changeset ovchinnikova:1
CREATE TABLE IF NOT EXISTS ticket (
    id SERIAL PRIMARY KEY,
    rout TEXT,
    dataTime DATE,
    seat TEXT,
    price INTEGER
);

-- changeset ovchinnikova:2
CREATE TABLE IF NOT EXISTS rout (
    id SERIAL PRIMARY KEY,
    departure TEXT,
    arrival TEXT,
    carrier TEXT,
    duration INTEGER
);

-- changeset ovchinnikova:3
CREATE TABLE IF NOT EXISTS carrier (
    id SERIAL PRIMARY KEY,
    name TEXT,
    phone TEXT
);

-- changeset ovchinnikova:4
CREATE TABLE IF NOT EXISTS "app_user" (
    id SERIAL PRIMARY KEY,
    login TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    fullName TEXT
);

-- changeset ovchinnikova:5
ALTER TABLE ticket ADD COLUMN rout_id INTEGER;
ALTER TABLE ticket ADD CONSTRAINT fk_rout FOREIGN KEY (rout_id) REFERENCES rout(id);

-- changeset ovchinnikova:6
ALTER TABLE ticket ADD COLUMN status VARCHAR(20) DEFAULT 'available';

-- changeset ovchinnikova:7
ALTER TABLE ticket DROP COLUMN rout;

-- changeset ovchinnikova:8
ALTER TABLE rout RENAME TO route;

-- changeset ovchinnikova:9
ALTER TABLE ticket RENAME COLUMN rout_id TO route_id;
-- changeset ovchinnikova:10
ALTER TABLE ticket ADD COLUMN user_id INTEGER;
