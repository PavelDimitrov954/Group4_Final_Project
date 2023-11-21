INSERT INTO virtual_teacher.roles (name)
VALUES ('student');
INSERT INTO virtual_teacher.roles (name)
VALUES ('teacher');
INSERT INTO virtual_teacher.roles (name)
VALUES ('unapproved_teacher');
INSERT INTO virtual_teacher.roles (name)
VALUES ( 'admin');
INSERT INTO virtual_teacher.users ( first_name, last_name, email,password,create_at,update_at)
VALUES ('Todor', 'Andonov', 'todor@company.com','pass4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


