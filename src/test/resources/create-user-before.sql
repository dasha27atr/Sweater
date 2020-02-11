delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '$2a$08$lINJ9WCSWEECuxwwKLRXI.C4kY5nqBSK.4uaRoMhlKCsRjE0q5TGG', 'april'),
(2, true, '$2a$08$lINJ9WCSWEECuxwwKLRXI.C4kY5nqBSK.4uaRoMhlKCsRjE0q5TGG', 'mike');

insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');