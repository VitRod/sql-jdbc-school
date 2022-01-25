package com.fox.dao.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fox.dao.ConnectionProvider;
import com.fox.dao.SqlScriptRunner;
import com.fox.dao.jdbc.JdbcCourseDao;
import com.fox.entity.Course;
import com.fox.exception.DAOException;

class JdbcCourseDaoTest {
	
    private static final String FILENAME_STARTUP_SCRIPT = "test_schema.sql";
    private static final String FILENAME_FINISH_SCRIPT = "drop_all_tables.sql";
    private static final String TEST_COURSE_NAME = "TestCourse";
    private static final String TEST_COURSE_DESCRIPTION = "testCourse description";
    private static final String COURSE_NAME_ID1 = "math";
    private static final String COURSE_NAME_ID2 = "biology";
    private static final String COURSE_DESCRIPTION_ID1 = "course of Mathematics";
    private static final String COURSE_DESCRIPTION_ID2 = "course of Biology";

    private JdbcCourseDao courseDao;
    private Course courseId1;
    private Course courseId2;

    @BeforeEach
    void setUp() throws Exception {
        courseDao = new JdbcCourseDao();
        
        courseId1 = new Course(1, COURSE_NAME_ID1, COURSE_DESCRIPTION_ID1);
        courseId2 = new Course(2, COURSE_NAME_ID2, COURSE_DESCRIPTION_ID2);
        
        
        try (Connection connection = ConnectionProvider.getConnection()) {
            SqlScriptRunner scriptRunner = new SqlScriptRunner(connection);
            scriptRunner.runSqlScript(FILENAME_STARTUP_SCRIPT);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (Connection connection = ConnectionProvider.getConnection()) {
            SqlScriptRunner scriptRunner = new SqlScriptRunner(connection);
            scriptRunner.runSqlScript(FILENAME_FINISH_SCRIPT);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

        @Test
        @DisplayName("given add course then create new record in testDB with id=3")
        void  givenCourse_whenAdd_thenAddCourse() throws DAOException {
            Course course = new Course(TEST_COURSE_NAME,
                    TEST_COURSE_DESCRIPTION);
            courseDao.add(course);
            String expectedCourseName = TEST_COURSE_NAME;
            String actualCourseName = courseDao.getById(3).get()
                    .getName();
            assertEquals(expectedCourseName, actualCourseName);
        }

        @Test
        @DisplayName("get course by id=1 should return 'math' course")
        void givenCourse_whenGetById_thenReturnCourse() throws DAOException {
            String expectedCourseName = COURSE_NAME_ID1;
            String actualCourseName = courseDao.getById(1).get()
                    .getName();
            assertEquals(expectedCourseName, actualCourseName);
        }

        @Test
        @DisplayName("get course by non-existing id=6 should return empty Optional")
        void   givenCourse_whenGetById_thenReturnEmptyOptional() throws DAOException {
            Optional<Course> expected = Optional.empty();
            Optional<Course> actual = courseDao.getById(6);
            assertEquals(expected, actual);
        }


        @Test
        @DisplayName("get all courses from base should return all courses")
        void givenCourses_whenAdd_thenReturnAllCourses() throws DAOException {
            List<Course> expectedCourses = new ArrayList<>();
            expectedCourses.add(courseId1);
            expectedCourses.add(courseId2);

            assertEquals(expectedCourses, courseDao.getAll());
        }

        @Test
        @DisplayName("update name course id=1 should write new name and getById(1) return new name")
        void givenNewCourse_whenUpdate_thenReturnNewUpdatedCourse() throws DAOException {
            Course newCourse = new Course(1, TEST_COURSE_NAME,
                    TEST_COURSE_DESCRIPTION);
            courseDao.update(newCourse);
            assertEquals(newCourse, courseDao.getById(1).get());
        }


        @Test
        @DisplayName("delete course id=1 should delete course and getById(1) return empty Optional")
        void  givenCourse_whenDeleteCourseById_thenReturnEmptyOptional() throws DAOException {
            courseDao.delete(courseId1);
            Optional<Course> expected = Optional.empty();
            Optional<Course> actual = courseDao.getById(1);
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("get courses for student_id=2 should return list courses with one course id=2")
        void givenCourses_whenAddCourse_thenGetCoursesForStudent2() throws DAOException {
            List<Course> courses = new ArrayList<>();
            courses.add(courseId2);
            assertEquals(courses, courseDao.getCoursesForStudentId(2));
        }
    
        @Test
        @DisplayName("get missing courses for student_id=2 should return list courses with one course id=1")
        void givenCourses_whenAddCourse_thenGetCoursesMissingForStudent1() throws DAOException {
            List<Course> courses = new ArrayList<>();
            courses.add(courseId1);
            assertEquals(courses, courseDao.getCoursesMissingForStudentId(2));
        }
}
