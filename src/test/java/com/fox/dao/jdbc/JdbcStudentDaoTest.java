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
import com.fox.dao.jdbc.JdbcStudentDao;
import com.fox.entity.Student;
import com.fox.exception.DAOException;

class JdbcStudentDaoTest {
	
    private static final String FILENAME_STARTUP_SCRIPT = "test_schema.sql";
    private static final String FILENAME_FINISH_SCRIPT = "drop_all_tables.sql";
    private static final String TEST_FIRST_NAME = "TestFirstName";
    private static final String TEST_LAST_NAME = "TestLastName";
    private static final String COURSE_ID1_NAME = "math";
    private static final String COURSE_ID2_NAME = "biology";

    private JdbcStudentDao studentDao;
     Student studentId1;
     Student studentId2;

    @BeforeEach
    void setUp() throws Exception {
        studentDao = new JdbcStudentDao();

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
        @DisplayName("add student should create new record in testDB with id=3")
        void givenStudent_whenAddStudent_thenReturnNewStudent() throws DAOException {
        	int expectedStudentId = 3;
            Student student = new Student(expectedStudentId, TEST_FIRST_NAME, TEST_LAST_NAME);
            studentDao.add(student);
            assertEquals(student, studentDao.getById(expectedStudentId).get());
        }


        @Test
        @DisplayName("get student by id=1 should return '1, 1, John Doe' student")
        void givenStudent_whenGetStudentById_thenReturnStudent() throws DAOException {
        	studentId1 = new Student(1, 1, "John", "Doe");
            Student actualStudent = studentDao.getById(1).get();
            assertEquals(studentId1, actualStudent);
        }

        @Test
        @DisplayName("get student by non-existing id=6 should return empty Optional")
        void  givenStudents_whenGetStudentByNonExistingId_thenReturnEmptyOptional() throws DAOException {
            Optional<Student> expected = Optional.empty();
            Optional<Student> actual = studentDao.getById(6);
            assertEquals(expected, actual);
        }


        @Test
        @DisplayName("get all students from base should return all 2 students")
        void givenStudents_whenGetAllStudents_thenReturnAllStudents()  throws DAOException {
            List<Student> expectedStudents = getStudentTestData();
            assertEquals(expectedStudents, studentDao.getAll());
        }

        List<Student> getStudentTestData () {
            List<Student> result = new ArrayList<>();
            studentId1 = new Student(1, 1, "John", "Doe");
            studentId2 = new Student(2, 3, "Jane", "Does");
            result.add(studentId1);
            result.add(studentId2);
            return result;
        }

        @Test
        @DisplayName("update names student id=1 with groupId should write new names and getById(1) return new names")
        void givenStudentWithGroup_whenUpdateStudent1WithGroupId_thenReturnNewStudentWithGroupId() throws DAOException {
            Student newStudent = new Student(1, 1, TEST_FIRST_NAME,
                    TEST_LAST_NAME);
            studentDao.update(newStudent);
            assertEquals(newStudent, studentDao.getById(1).get());
        }
        

        @Test
        @DisplayName("update names student id=1 without groupId should write new names and getById(1) return new names")
        void   givenStudentWithoutGroup_whenUpdateStudent1WithoutGroupId_thenReturnNewStudent() throws DAOException {
            Student newStudent = new Student(1, TEST_FIRST_NAME,
                    TEST_LAST_NAME);
            studentDao.update(newStudent);
            assertEquals(newStudent, studentDao.getById(1).get());
        }

        @Test
        @DisplayName("delete student id=1 should delete student and getById(1) return empty Optional")
        void givenCourse_whenDeleteCourseById_thenReturnEmptyOptional() throws DAOException {
        	studentId1 = new Student(1, 1, "John", "Doe");
            studentDao.delete(studentId1);
            Optional<Student> expected = Optional.empty();
            Optional<Student> actual = studentDao.getById(1);
            assertEquals(expected, actual);
        }
        
        

        @Test
        @DisplayName("remove student id=2 from course id=2 should remove record from table students_courses")
        void givenStudents_whenRemoveStudentId2FromCourseId2_thenRemoveRecord() throws DAOException {
        	int studentId = 2;     	
            studentDao.removeStudentFromCourse(studentId, 2);
            List<Student> students = studentDao
                    .getStudentsWithCourseName(COURSE_ID2_NAME);
            Student actualStudent = students.stream()
                    .filter(s -> s.getId() == studentId).findAny()
                    .orElse(null);
            assertNull(actualStudent);
        }

        @Test
        @DisplayName("get students from course 'math' should return student id=1")
        void  givenStudents_whenGetStudentsWithCourseMath_thenReturnStudent() throws DAOException {
            List<Student> students = new ArrayList<>();
            studentId1 = new Student(1, 1, "John", "Doe");
            students.add(studentId1);

            assertEquals(students,
                    studentDao.getStudentsWithCourseName(COURSE_ID1_NAME));
        }
}
