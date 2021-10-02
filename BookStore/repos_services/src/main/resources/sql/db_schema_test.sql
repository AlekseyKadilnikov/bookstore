create table if not exists book
(
    id int primary key auto_increment,
    name varchar(50) not null,
    publisher varchar(50) not null,
    year int not null,
    price decimal(12,2) not null,
    count int not null,
    date_of_receipt date not null,
    description varchar(255)
);

create table if not exists hibernate_sequence
(
    next_val int
);


create table if not exists author
(
    id int primary key auto_increment,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    middle_name varchar(50)
);


create table if not exists user
(
    id int primary key auto_increment,
    name varchar(50) not null,
    password varchar(255) not null,
    role varchar(20) not null,
    status varchar(50) not null,
    email varchar(50) not null
);

create table if not exists order_t
(
    id int primary key auto_increment,
    user_id int not null,
    total_price int not null,
    init_date datetime not null,
    exec_date datetime,
    status_code int not null,
    foreign key (user_id) references user(id)
);

create table if not exists request
(
    id int primary key auto_increment,
    name varchar(255) not null,
    status_code int not null,
    count int not null
);

create table if not exists order_book
(
    order_id int not null,
    book_id int not null,
    book_count int not null,
    primary key (order_id, book_id),
    FOREIGN KEY (book_id) REFERENCES book(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (order_id) REFERENCES order_t(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

create table if not exists book_request
(
    book_id int not null,
    request_id int not null,
    primary key (book_id, request_id),
    FOREIGN KEY (book_id) REFERENCES book(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (request_id) REFERENCES request(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

create table if not exists author_book
(
    book_id int not null,
    author_id int not null,
    primary key (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES book(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (author_id) REFERENCES author(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

insert into book
(name, publisher, year, price, count, date_of_receipt, description)
values
    ('Идиот', 'Наука', 1990, 350, 0, '2020-11-11', 'd0'),
    ('Бесы', 'Наука', 1988, 370, 0, '2020-09-11', 'd1');

insert into hibernate_sequence
(next_val)
values
    (100)