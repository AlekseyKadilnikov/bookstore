use bookstore;

create table if not exists user
(
    id       int primary key auto_increment,
    name     varchar(50)  not null,
    password varchar(255) not null
);

create table if not exists role
(
    id   int primary key auto_increment,
    name varchar(255) not null
);

create table if not exists permission
(
    id   int primary key auto_increment,
    name varchar(255) not null
);

create table if not exists user_role
(
    user_id int not null,
    role_id int not null,
    primary key (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

create table if not exists role_permission
(
    role_id       int not null,
    permission_id int not null,
    primary key (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

create table if not exists order_t
(
    id          int primary key auto_increment,
    user_id     int     not null,
    total_price int      not null,
    init_date   datetime not null,
    exec_date   datetime,
    status_code int      not null,
    foreign key (user_id) references user (id)
);

create table if not exists order_book
(
    order_id   int not null,
    book_id    int not null,
    book_count int  not null,
    primary key (order_id, book_id),
    FOREIGN KEY (book_id) REFERENCES book (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (order_id) REFERENCES order_t (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

