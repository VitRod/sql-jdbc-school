package com.fox.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.fox.dao.StudentDao;
import com.fox.domain.generator.Generator;
import com.fox.entity.Student;
import com.fox.exception.DAOException;
import com.fox.exception.DomainException;

public class StudentService {
	
    private static final String MASK_MESSAGE_ADD_EXCEPTION = "Don't save student %s in base";
    private static final String MESSAGE_GET_EXCEPTION = "Can't get students";
    private static final String MESSAGE_DELETE_EXCEPTION = "Can't delete student";
    private static final String MESSAGE_ADD_STUDENT_COURSE_EXCEPTION = "Can't add student to course";
    private static final String MASK_MESSAGE_ADD_STUDENT_COURSE_EXCEPTION = "Don't save student %d and course %d";
    private static final String MASK_MESSAGE_DELETE_STUDENT_COURSE_EXCEPTION = "Can't delete student %d from course %d";

    private StudentDao studentDao;
    private Generator<Student> generator;
    private Random random;

    public StudentService(StudentDao studentDao, Generator<Student> generator,
            Random random) {
        this.studentDao = studentDao;
        this.generator = generator;
        this.random = random;
    }

    public void createTestStudents(int numberStudents) {
        List<Student> students = generator.generate(numberStudents);
        students.forEach(this::addStudentToBase);
    }

    public void createTestStudentsCourses(int numberStudents) {
        List<Integer[]> studentCourses = new ArrayList<>();
        for (int i = 0; i < numberStudents; i++) {
            assignCourses(i + 1, studentCourses);
        }
        addStudentsCoursesToBase(studentCourses);
    }

    public List<Student> getStudentsWithCourseName(String courseName) {
        try {
            return studentDao.getStudentsWithCourseName(courseName);
        } catch (DAOException e) {
            throw new DomainException(MESSAGE_GET_EXCEPTION, e);
        }
    }

    public void createStudent(String firstName, String lastName) {
        Student student = new Student(firstName, lastName);
        addStudentToBase(student);
    }

    public void addStudentToBase(Student student) {
        try {
            studentDao.add(student);
        } catch (DAOException e) {
            throw new DomainException(
                    String.format(MASK_MESSAGE_ADD_EXCEPTION, student), e);
        }
    }

    public void deleteById(int id) {
        Student student = new Student(id);
        try {
            studentDao.delete(student);
        } catch (DAOException e) {
            throw new DomainException(MESSAGE_DELETE_EXCEPTION, e);
        }
    }

    public void addStudentToCourse(int studentId, int courseId) {
        try {
            studentDao.addStudentCourse(studentId, courseId);
        } catch (DAOException e) {
            throw new DomainException(MESSAGE_ADD_STUDENT_COURSE_EXCEPTION, e);
        }
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        try {
            studentDao.removeStudentFromCourse(studentId, courseId);
        } catch (DAOException e) {
            throw new DomainException(
                    String.format(MASK_MESSAGE_DELETE_STUDENT_COURSE_EXCEPTION,
                            studentId, courseId),
                    e);
        }
    }

    
    @SuppressWarnings("java:S5413")
    private void assignCourses(int studentId, List<Integer[]> studentCourses) {
        int numberCoursesPerStudent = random.nextInt(2) + 1;
        List<Integer> coursesId = new LinkedList<>();
        Collections.addAll(coursesId, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        for (int i = 0; i < numberCoursesPerStudent; i++) {
            int numberCourse = random.nextInt(coursesId.size());
            Integer[] studentCourse = { studentId,
                    coursesId.get(numberCourse) };
            studentCourses.add(studentCourse);
            coursesId.remove(numberCourse);
        }
    }

    private void addStudentsCoursesToBase(List<Integer[]> studentsCourses) {
        studentsCourses.forEach(pair -> {
            try {
                studentDao.addStudentCourse(pair[0], pair[1]);
            } catch (DAOException e) {
                throw new DomainException(String.format(
                        MASK_MESSAGE_ADD_STUDENT_COURSE_EXCEPTION, pair[0], pair[1]), e);
            }
        });
    }
}
