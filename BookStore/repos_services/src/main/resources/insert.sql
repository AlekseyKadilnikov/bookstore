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

# insert into book
# (name, publisher, year, price, count, date_of_receipt, description)
# values
#     ('Идиот', 'Наука', 1990, 350, 0, '2020-11-11', 'd0'),
#     ('Бесы', 'Наука', 1988, 370, 0, '2020-09-11', 'd1');