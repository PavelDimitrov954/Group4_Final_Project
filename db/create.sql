create database virtual_teacher;

use virtual_teacher;
CREATE TABLE users
(
    user_id         INT PRIMARY KEY AUTO_INCREMENT,
    first_name      VARCHAR(20)         NOT NULL,
    last_name       VARCHAR(20)         NOT NULL,
    email           VARCHAR(255) UNIQUE NOT NULL,
    password        VARCHAR(255)        NOT NULL,
    profile_picture BLOB,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE roles
(   role_id         INT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(20)         NOT NULL

   );


CREATE TABLE roles_users
(
    user_id INT ,
    role_id INT ,
        FOREIGN KEY (user_id) REFERENCES users (user_id),
        FOREIGN KEY (role_id) REFERENCES roles (role_id)

);



CREATE TABLE course_topics
(
    topic_id INT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL
);

CREATE TABLE courses
(
    course_id   INT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(50) NOT NULL,
    topic_id    INT,
    description TEXT,
    start_date  DATE,
    status      VARCHAR(50) NOT NULL,
    teacher_id  INT,

    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES users (user_id),
    FOREIGN KEY (topic_id) REFERENCES course_topics (topic_id)
);

CREATE TABLE lectures
(
    lecture_id  INT PRIMARY KEY AUTO_INCREMENT,
    course_id   INT         NOT NULL,
    title       VARCHAR(50) NOT NULL,
    description TEXT,
    video_link  TEXT,
    status      VARCHAR(50) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses (course_id)
);

CREATE TABLE assignments
(
    assignment_id INT PRIMARY KEY AUTO_INCREMENT,
    lecture_id    INT          NOT NULL,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lecture_id) REFERENCES lectures (lecture_id)
);

CREATE TABLE submissions
(
    submission_id  INT PRIMARY KEY AUTO_INCREMENT,
    student_id      INT NOT NULL,
    submissionFile BLOB,
    grade          DECIMAL,
    submittedAt    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (Student_id) REFERENCES users (user_id)
);

CREATE TABLE assignment_submissions
(
    submission_id  INT NOT NULL ,
    assignment_id   INT NOT NULL,
    FOREIGN KEY (submission_id) REFERENCES submissions (submission_id),
    FOREIGN KEY (assignment_id) REFERENCES assignments (assignment_id)

);

CREATE TABLE ratings
(
    rating_id  INT PRIMARY KEY AUTO_INCREMENT,
    course_id  INT NOT NULL,
    student_id INT NOT NULL,
    score      INT NOT NULL,
    comment    TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses (course_id),
    FOREIGN KEY (student_id) REFERENCES users (user_id)
);

CREATE TABLE enrollments
(
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    course_id     INT NOT NULL,
    student_id    INT NOT NULL,
    enrolled_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses (course_id),
    FOREIGN KEY (student_id) REFERENCES users (user_id)
);

CREATE TABLE notes
(
    note_id INT PRIMARY KEY AUTO_INCREMENT,
    lecture_id INT NOT NULL,
    student_id INT NOT NULL,
    content TEXT,
    FOREIGN KEY (lecture_id) REFERENCES lectures(lecture_id),
    FOREIGN KEY (student_id) REFERENCES users(user_id)
);
