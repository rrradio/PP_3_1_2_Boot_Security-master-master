USE mmschema;

CREATE SCHEMA IF NOT EXISTS mmschema ;

Use mmschema;

CREATE TABLE users (
                       id bigint primary key auto_increment,
                       username varchar(100) not null,
                       age int not null,
                       email varchar(100) not null,
                       password varchar(255) not null
);

INSERT INTO users (username, age, email, password) VALUES
                                                       ('admin', 25, 'mmm@mail.ru', '$2a$04$qH/jbzKbOnmKUwlsLcUuB.lYQ.rlNejvt7wGfVN0UpZI70QeSALUG'),
                                                       ('user', 33, 'kkk@mail.ru', '$2a$04$i7DNcOLpqu1V.lBwj8CUgufYpepQWL2D00REZ3soCSFh/2hvwiFYy');

CREATE TABLE roles (
                       id int primary key auto_increment,
                       name varchar(50) not null
);

CREATE TABLE users_roles (
                             user_id bigint not null ,
                             role_id int not null ,
                             primary key (user_id, role_id),
                             foreign key (user_id) references users(id),
                             foreign key (role_id) references roles(id)
);

INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users_roles (user_id, role_id) VALUES (1,2);
INSERT INTO users_roles (user_id, role_id) VALUES (2,1);