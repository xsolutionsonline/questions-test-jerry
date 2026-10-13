CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    first_name varchar(60) NULL,
    last_name varchar(60) NULL,
    surname varchar(60) NULL,
    phone varchar(60) NULL
);
