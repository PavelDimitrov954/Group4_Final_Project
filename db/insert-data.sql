INSERT INTO virtual_teacher.roles (name)
VALUES ('STUDENT'),
       ('TEACHER'),
       ('UNAPPROVED_TEACHER'),
       ( 'ADMIN');
INSERT INTO virtual_teacher.users ( first_name, last_name, email,password,create_at,update_at)
VALUES ('Todor', 'Andonov', 'todor@company.com','pass4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO courses (title, topic_id, description, start_date, status, teacher_id, created_at, updated_at)
VALUES ('Introduction to Java', 1, 'This course covers basic Java programming concepts.', '2021-01-15', 'published', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO course_topics(name)
VALUE ('ADVANCED');

INSERT INTO virtual_teacher.courses (title, topic_id, description, start_date, status, teacher_id)
VALUES ('Advanced Python', 2, 'Learn advanced topics in Python programming.', '2021-02-01', 'draft', 102);

INSERT INTO virtual_teacher.courses (title, topic_id, description, start_date, status, teacher_id)
VALUES ('Database Fundamentals', 3, 'Understanding the basics of database management.', '2021-03-20', 'published', 103);

-- Add as many as you need

INSERT INTO virtual_teacher.courses ( id, title, description, start_date,
                                     status,teacher_id, created_at,updated_at )
VALUES (1, 'TitleCourse',
        'DescriptionCourse',timestamp '2024-10-12 21:22:23', 'StatusCourse',1, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);


