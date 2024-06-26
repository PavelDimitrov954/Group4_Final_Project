# Project Description

**Virtual Teacher** is an online platform for tutoring. Users will be able to register as either a teacher or a student.

- **Students** must be able to enroll for video courses, watch the available video lectures. After finishing each video within a course, students will be asked to submit an assignment, which will be graded by the teacher. After a successful completion of a course, the students must be able to rate it.
- **Teachers** should be able to create courses and upload video lectures with assignments. Each assignment will be graded individually after submission from the students. Users can become teachers only after approval from an administrator.

## Functional Requirements

### Entities

- Each user (teacher or student) must have a profile picture, first and last name, email, and a password.
    - The email will serve as their username so it must be a valid email and unique in the application.
    - The password must be at least 8 symbols and should contain capital letter, digit, and special symbol.
    - The first and last names must be between 2 and 20 characters long.
- Courses must have a title, a topic, a description, and a list of lectures. They should also have a starting date.
    - The title is a string between 5 and 50 characters long.
    - The topic is one of a predefined range like Science, History, Literature, and others.
    - The description is optional and can be at most 1000 symbols in length.
    - The starting date, if set, makes the course visible to the students but they cannot enroll before the said date.
- Each lecture must have, a title, a description, a video, and an assignment.
    - The title is a string between 5 and 50 characters long.
    - The description is optional and can be at most 1000 symbols in length.
    - The video must be embedded (for example from YouTube).
    - The assignment must be a text file, saved to the file system, or optionally, could be uploaded to a cloud file hosting service.
- Search bar, allowing the student to search for additional information in Wikipedia.

## Public Section

The public part must be visible without authentication.

- Anonymous users must be able to register as students or teachers.
- The anonymous users must also be able to browse the catalog of available courses. The user must be able to filter the catalog by course name, course topic, teacher, and rating, as well as to sort the catalog by name and/or rating.
- Anonymous users must not be able to enroll for a course.

## Private Part (Students)

Must be accessible only if the student is authenticated.

### Users

Users must have private section (profile page).

Users must be able to:
- View and edit their profile (names and picture).
- Change their password.
- Enroll for courses.
- See their enrolled and completed courses.

Users could be able to:
- Take notes in plain text for each lecture.

### Courses

- The videos in a course must be accessible only after enrollment.
- Students must submit their work as a text file (txt, doc, docx, etc.).
- Students must be able to see the grade they’ve received for the assignment and their average grade for the course (formed by the average of all assignments grades).
- After receiving a grade for their last assignment for the course, if their average grade is above the passing grade, defined on per course basis, they should be able to leave a rating for the course.

## Private Part (Teachers)

Teachers must have access to all the functionality that students do.

However, after they are approved by an administrator, a course administration page must be accessible to them. On it they must be able to modify courses and have a section where they can search for students.

Courses must be either drafts or published. Draft courses are visible to the teachers, but not to the students. Once the teacher has prepared the course, they must be able to publish it and it becomes available to all students.

Courses could have a comments page, where students can comment on the lectures and ask questions.

## Administration Part

Administrators must have access to all the functionality that teachers do.

Administrators must not be registered though the ordinary registration process. They can be given administrator rights from the UI by other administrators only.

They must be able to modify/delete courses and users, approve administrators and teachers.

The REST API provides the following capabilities:
- **Users**
    - CRUD operations 
    - Enroll to courses 
    - Search by first, last name or email 
- **Courses**
    - CRUD operations 
    - Rate 
- **Lectures**
    - CRUD operations
    - Submit assignment file
