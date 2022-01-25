CREATE TABLE groups
(
    group_id SERIAL NOT NULL,
    group_name character varying(10) NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (group_id)
);

CREATE TABLE students
(
    student_id SERIAL NOT NULL,
    group_id integer,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    CONSTRAINT students_pkey PRIMARY KEY (student_id),
    CONSTRAINT group_id FOREIGN KEY (group_id)
        REFERENCES public.groups (group_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);
	
CREATE TABLE courses
(
    course_id SERIAL NOT NULL,
    course_name character varying(50) NOT NULL,
    course_description character varying,
    CONSTRAINT courses_pkey PRIMARY KEY (course_id)
);

CREATE TABLE students_courses
(
    student_id integer NOT NULL,
    course_id integer NOT NULL,
    CONSTRAINT students_courses_pkey PRIMARY KEY (student_id, course_id),
    CONSTRAINT course_id FOREIGN KEY (course_id) REFERENCES public.courses (course_id)
    ON UPDATE NO ACTION
    ON DELETE CASCADE,
    CONSTRAINT student_id FOREIGN KEY (student_id) REFERENCES public.students (student_id)
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);

insert into groups (group_name) values ('OR-41');
insert into groups (group_name) values ('GM-87');
insert into groups (group_name) values ('XI-12');

insert into students (group_id, first_name, last_name) values (1, 'John', 'Doe');
insert into students (group_id, first_name, last_name) values (3, 'Jane', 'Does');


insert into courses (course_name, course_description) values ('math', 'course of Mathematics');
insert into courses (course_name, course_description) values ('biology', 'course of Biology');

insert into students_courses (student_id, course_id) values (1, 1);
insert into students_courses (student_id, course_id) values (1, 2);
insert into students_courses (student_id, course_id) values (2, 2);

