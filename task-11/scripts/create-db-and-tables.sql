create database if not exists productsdb;
use productsdb;

create table if not exists Product 
(
	model varchar(50) primary key,
    marker varchar(10) not null,
    type varchar(50) not null
);

create table if not exists PC
(
	code int primary key auto_increment,
    model varchar(50) not null,
    speed smallint not null,
    ram smallint not null,
    hd real not null,
    cd varchar(10) not null,
    price decimal(12, 2),
    foreign key (model) references Product(model)
);

create table if not exists Laptop
(
	code int primary key auto_increment,
    model varchar(50) not null,
    speed smallint not null,
    ram smallint not null,
    hd real not null,
    price decimal(12, 2),
    screen tinyint not null,
    foreign key (model) references Product(model)
);

create table if not exists Printer
(
	code int primary key auto_increment,
    model varchar(50) not null,
    color char(1) not null,
    type varchar(10) not null,
    price decimal(12, 2),
    foreign key (model) references Product(model)
);