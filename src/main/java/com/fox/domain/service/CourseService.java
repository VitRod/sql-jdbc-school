package com.fox.domain.service;

import java.util.List;

import com.fox.dao.CourseDao;
import com.fox.domain.generator.Generator;
import com.fox.entity.Course;
import com.fox.exception.DAOException;
import com.fox.exception.DomainException;

public class CourseService {
	
    private static final String MASK_MESSAGE_ADD_EXCEPTION = "Don't save course %s in base";
    private static final String MESSAGE_GET_EXCEPTION = "Can't get courses";

    private CourseDao courseDao;
    private Generator<Course> generator;

    public CourseService(CourseDao courseDao, Generator<Course> generator) {
        this.courseDao = courseDao;
        this.generator = generator;
    }

    public void createTestCourses(int numberCourses) {
        List<Course> courses = generator.generate(numberCourses);
        courses.forEach(this::addCourseToBase);
    }

    public List<Course> getCoursesMissingForStudent(int studentId) {
        try {
            return courseDao.getCoursesMissingForStudentId(studentId);
        } catch (DAOException e) {
            throw new DomainException(MESSAGE_GET_EXCEPTION, e);
        }
    }

    public List<Course> getCoursesForStudent(int studentId) {
        try {
            return courseDao.getCoursesForStudentId(studentId);
        } catch (DAOException e) {
            throw new DomainException(MESSAGE_GET_EXCEPTION, e);
        }
    }

    public void addCourseToBase(Course course) {
        try {
            courseDao.add(course);
        } catch (DAOException e) {
            throw new DomainException(
                    String.format(MASK_MESSAGE_ADD_EXCEPTION, course), e);
        }
    }
}
