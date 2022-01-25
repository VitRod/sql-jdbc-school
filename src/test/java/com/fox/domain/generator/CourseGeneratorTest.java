package com.fox.domain.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fox.entity.Course;
import com.fox.exception.DAOException;

class CourseGeneratorTest {
	
    private static final String COURSE_MATH_NAME = "math";
    private static final String COURSE_MATH_DESCRIPTION = "course of Mathematics";
    private static final String COURSE_BIOLOGY_NAME = "biology";
    private static final String COURSE_BIOLOGY_DESCRIPTION = "course of Biology";
    private CourseGenerator generator;

    @BeforeEach
    void setUp() throws Exception {
        generator = new CourseGenerator();
    }

    @Test
    @DisplayName("test generator with number course = 2 should return List with 2 courses")
    void  givenTest_whenGenerateCourses_thenReturnListOf2Courses() throws DAOException {
        List<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(new Course(COURSE_MATH_NAME, COURSE_MATH_DESCRIPTION));
        expectedCourses.add(new Course(COURSE_BIOLOGY_NAME, COURSE_BIOLOGY_DESCRIPTION));
        
        List<Course> actualCourses = generator.generate(2);
        assertEquals(expectedCourses, actualCourses);
    }

}
