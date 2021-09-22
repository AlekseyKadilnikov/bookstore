use bookstore;

insert into user (name, password)
values ('admin', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.');

insert into role (name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');

insert into user_role (user_id, role_id)
values (1, 1);

insert into permission (name)
values ('PERM_GET_ORDER'),
       ('PERM_UPDATE_ORDER'),
       ('PERM_DELETE_ORDER'),
       ('PERM_CREATE_ORDER'),
       ('PERM_GET_BOOK'),
       ('PERM_UPDATE_BOOK'),
       ('PERM_DELETE_BOOK'),
       ('PERM_CREATE_BOOK'),
       ('PERM_GET_REQUEST'),
       ('PERM_UPDATE_REQUEST'),
       ('PERM_DELETE_REQUEST'),
       ('PERM_CREATE_REQUEST'),
       ('PERM_GET_USER'),
       ('PERM_UPDATE_USER'),
       ('PERM_DELETE_USER'),
       ('PERM_CREATE_USER');

insert into role_permission (role_id, permission_id)
values (1,1),
       (1,2),
       (1,3),
       (1,4),
       (1,5),
       (1,6),
       (1,7),
       (1,8),
       (1,9),
       (1,10),
       (1,11),
       (1,12),
       (1,13),
       (1,14),
       (1,15),
       (1,16);

