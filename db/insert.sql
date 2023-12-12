use virtual_teacher;


INSERT INTO course_topics (name) VALUES ('Mathematics');
INSERT INTO course_topics (name) VALUES ('Science');
INSERT INTO course_topics (name) VALUES ('Literature');

-- Insert into roles
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('STUDENT');
INSERT INTO roles (name) VALUES ('TEACHER');
INSERT INTO roles (name) VALUES ('UNAPPROVED_TEACHER');

-- Insert into users (including two teachers and two students)
INSERT INTO users (create_at, email, first_name, last_name, password, update_at) VALUES (NOW(), 'john.doe@example.com', 'John', 'Doe','$2a$10$hHR3DNTCu15fZ8A.7dewPu4/9BBeRsWj/tU4ciQNJLeTinCdnfSwC', NOW());
INSERT INTO users (create_at, email, first_name, last_name, password, update_at) VALUES (NOW(), 'jane.doe@example.com', 'Jane', 'Doe', '$2a$10$hHR3DNTCu15fZ8A.7dewPu4/9BBeRsWj/tU4ciQNJLeTinCdnfSwC', NOW());
INSERT INTO users (create_at, email, first_name, last_name, password, update_at) VALUES (NOW(), 'alice.student@example.com', 'Alice', 'Student','$2a$10$hHR3DNTCu15fZ8A.7dewPu4/9BBeRsWj/tU4ciQNJLeTinCdnfSwC', NOW());
INSERT INTO users (create_at, email, first_name, last_name, password, update_at) VALUES (NOW(), 'bob.student@example.com', 'Bob', 'Student', '$2a$10$hHR3DNTCu15fZ8A.7dewPu4/9BBeRsWj/tU4ciQNJLeTinCdnfSwC', NOW());

-- Assign roles to users
INSERT INTO roles_users (user_id, role_id) VALUES (1, 3); -- John as a teacher
INSERT INTO roles_users (user_id, role_id) VALUES (2, 3); -- Jane as a teacher
INSERT INTO roles_users (user_id, role_id) VALUES (3, 2); -- Alice as a student
INSERT INTO roles_users (user_id, role_id) VALUES (4, 2); -- Bob as a student

-- Insert into courses (with teacher_id as 1 and 2)
INSERT INTO courses (created_at, description, rating, start_date, status, title, updated_at, teacher_id, topic_id) VALUES (NOW(), 'Intro to Algebra', 5, '2021-09-01', 'ACTIVE', 'Algebra 101', NOW(), 1, 1);
INSERT INTO courses (created_at, description, rating, start_date, status, title, updated_at, teacher_id, topic_id) VALUES (NOW(), 'Basic Chemistry', 4, '2021-10-01', 'ACTIVE', 'Chemistry Basics', NOW(), 2, 2);

-- Insert into enrollments (linking students to courses)
INSERT INTO enrollments (enrolled_at, course_id, student_id) VALUES (NOW(), 1, 3);
INSERT INTO enrollments (enrolled_at, course_id, student_id) VALUES (NOW(), 2, 4);

-- Insert into lectures (linked to courses)
INSERT INTO lectures (created_at, description, title, updated_at, video_link, course_id) VALUES (NOW(), 'Algebraic expressions', 'Lecture 1', NOW(), 'http://linktolecture.com', 1);
INSERT INTO lectures (created_at, description, title, updated_at, video_link, course_id) VALUES (NOW(), 'Chemical bonds', 'Lecture 1', NOW(), 'http://linktolecture.com', 2);

-- Insert into assignments (linked to lectures)
INSERT INTO assignments (created_at, description, title, lecture_id) VALUES (NOW(), 'Solve the equations', 'Assignment 1', 1);
INSERT INTO assignments (created_at, description, title, lecture_id) VALUES (NOW(), 'Describe chemical bonding', 'Assignment 1', 2);

-- Insert into notes (linked to lectures and students)
INSERT INTO notes (content, lecture_id, student_id) VALUES ('My notes on algebraic expressions', 1, 3);
INSERT INTO notes (content, lecture_id, student_id) VALUES ('My notes on chemical bonds', 2, 4);

-- Insert into ratings (linked to courses and students)
INSERT INTO ratings (comment, created_at, score, course_id, student_id) VALUES ('Great course!', NOW(), 5, 1, 3);
INSERT INTO ratings (comment, created_at, score, course_id, student_id) VALUES ('Very informative', NOW(), 4, 2, 4);

-- Insert into submissions (linked to assignments and students)
INSERT INTO submissions (grade, submission_url, submitted_at, assignment_id, user_id) VALUES (90.5, 'http://submissionlink.com', NOW(), 1, 3);
INSERT INTO submissions (grade, submission_url, submitted_at, assignment_id, user_id) VALUES (85.0, 'http://submissionlink.com', NOW(), 2, 4);
