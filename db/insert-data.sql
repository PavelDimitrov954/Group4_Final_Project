INSERT INTO virtual_teacher.roles (name)
VALUES ('student'),
       ('teacher'),
       ('unapproved_teacher'),
       ( 'admin');
INSERT INTO virtual_teacher.users ( first_name, last_name, email,password,create_at,update_at)
VALUES ('Todor', 'Andonov', 'todor@company.com','pass4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO virtual_teacher.courses ( id, title, description, start_date,
                                     status,teacher_id, created_at,updated_at )
VALUES (1, 'TitleCourse',
        'DescriptionCourse',timestamp '2024-10-12 21:22:23', 'StatusCourse',1, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);


