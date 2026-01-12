CREATE TABLE singer (
                        id INT NOT NULL AUTO_INCREMENT
    , first_name VARCHAR(60) NOT NULL
    , last_name VARCHAR(40) NOT NULL
    , birth_date DATE
    , UNIQUE (first_name, last_name)
    , PRIMARY KEY (id)
);
insert into singer (id, first_name, last_name, birth_date) values (1, 'Nick', 'Drake', '1948-06-19');
insert into singer (id, first_name, last_name, birth_date) values (2, 'Ray', 'Charles', '1930-09-23');
insert into singer (id, first_name, last_name, birth_date) values (3, 'Dinah', 'Washington', '1924-08-29');
insert into singer (id, first_name, last_name, birth_date) values (4, 'John', 'Mayer', '1977-10-16');
insert into singer (id, first_name, last_name, birth_date) values (5, 'Ben', 'Barnes', '1981-08-20');
insert into singer (id, first_name, last_name, birth_date) values (6, 'John', 'Butler', '1975-04-01');
insert into singer (id, first_name, last_name, birth_date) values (7, 'Eric', 'Clapton', '1945-03-30');
