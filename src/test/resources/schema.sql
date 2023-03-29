CREATE TABLE `user`(id INT AUTO_INCREMENT,
    name VARCHAR(255),
    address varchar(255),
    age bigint not null,
    email varchar(255),
    primary key (`id`),
    UNIQUE (email)
);

