package com.fox.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fox.dao.jdbc.JdbcCourseDao;
import com.fox.dao.jdbc.JdbcGroupDao;
import com.fox.dao.jdbc.JdbcStudentDao;
import com.fox.entity.Course;
import com.fox.entity.Group;
import com.fox.entity.Student;
import com.fox.exception.DAOException;

class StartUpDaoTest {
	
    private static final String FILENAME_STARTUP_SCRIPT = "test_schema.sql";
    private static final String FILENAME_FINISH_SCRIPT = "drop_all_tables.sql";

    private StartUpDao startUpDao;

    @BeforeEach
    void setUp() throws Exception {
        startUpDao = new StartUpDao();

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
    @DisplayName("given test with prepareTables method should create empty table 'groups'")
    void givenPrepareTablesMethod_whenGetAll_thenEmptyTableGroupsCreated() throws DAOException {
        startUpDao.prepareTables();

        List<Group> expectedGroups = new ArrayList<>();
        JdbcGroupDao groupDao = new JdbcGroupDao();
        List<Group> actualGroups = groupDao.getAll();

        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    @DisplayName("given test with prepareTables method should create empty table 'students'")
    void givenPrepareTablesMethod_whenGetAll_thenEmptyTableStudentsCreated() throws DAOException {
        startUpDao.prepareTables();

        List<Student> expectedStudents = new ArrayList<>();
        JdbcStudentDao studentDao = new JdbcStudentDao();
        List<Student> actualStudents = studentDao.getAll();

        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    @DisplayName("given test with prepareTables method should create empty table 'courses'")
    void givenPrepareTablesMethod_whenGetAll_thenEmptyTableCoursesCreated() throws DAOException {
        startUpDao.prepareTables();

        List<Course> expectedCourses = new ArrayList<>();
        JdbcCourseDao courseDao = new JdbcCourseDao();
        List<Course> actualCourses = courseDao.getAll();

        assertEquals(expectedCourses, actualCourses);
    }

}
