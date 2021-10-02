TRUNCATE TABLE book;
TRUNCATE TABLE hibernate_sequence;

insert into book
(name, publisher, year, price, count, date_of_receipt, description)
values
    ('Идиот', 'Наука', 1990, 350, 0, '2020-11-11', 'd0'),
    ('Бесы', 'Наука', 1988, 370, 0, '2020-09-11', 'd1');

insert into hibernate_sequence
(next_val)
values
    (100)