package com.fox.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fox.dao.jdbc.JdbcStudentDao;
import com.fox.domain.generator.Generator;
import com.fox.entity.Student;
import com.fox.exception.DAOException;
import com.fox.exception.DomainException;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
	
    private static final String TEST_COURSE_NAME = "CourseName";
    private static final String TEST_FIRST_NAME = "First";
    private static final String TEST_LAST_NAME = "Last";
    private static final String MESSAGE_GET_EXCEPTION = "Can't get students";
    private static final String MESSAGE_CREATE_EXCEPTION = "Don't save student %s in base";
    private static final String MESSAGE_DELETE_EXCEPTION = "Can't delete student";
    private static final String MESSAGE_ADD_STUDENT_COURSE_EXCEPTION = "Can't add student to course";
    private static final String MESSAGE_DELETE_STUDENT_COURSE_EXCEPTION = "Can't delete student %d from course %d";

    private StudentService service;
    private List<Student> students;
    private Student student;
    private int numberStudents = 5;
    private int testStudentId = 2;
    private int testCourseId = 2;

    @Mock
    private JdbcStudentDao studentDaoMock;

    @Mock
    private Generator<Student> generatorMock;

    @Mock
    private Random randomMock;

    @BeforeEach
    void setUp() throws Exception {
        service = new StudentService(studentDaoMock, generatorMock, randomMock);
        students = new ArrayList<>();
        student = new Student(TEST_FIRST_NAME, TEST_LAST_NAME);
    }

    @Nested
    @DisplayName("test 'createTestStudents' method")
    class testCreateTestStudents {

        @Test
        @DisplayName("method should call generator.generate once")
        void  givenCreationOfTestStudent_whenGenerateNumberStudents_thenNubmerCallsGenerator() throws DAOException {
            service.createTestStudents(numberStudents);

            verify(generatorMock, times(1)).generate(numberStudents);
        }

        @Test
        @DisplayName("method should call studentDao as much time as numberStudents")
        void givenMockedStudent_whenAddStudent_thenNumberCallsDao() throws DAOException {
            for (int i = 0; i < numberStudents; i++) {
                students.add(student);
            }
            when(generatorMock.generate(numberStudents)).thenReturn(students);

            service.createTestStudents(numberStudents);

            verify(studentDaoMock, times(numberStudents)).add(student);
        }
    }



    @Nested
    @DisplayName("test getStudentsWithCourseName method")
    class testGetStudentsWithCourseName {

        @Test
        @DisplayName("normal input should return List<Sudent>")
        void givenMockedStudents_whenGetStudentsWithCourseName_thenReturnListStudent() throws DAOException {
            when(studentDaoMock.getStudentsWithCourseName(anyString()))
                    .thenReturn(students);
            List<Student> actualStudents = service
                    .getStudentsWithCourseName(anyString());
            assertEquals(students, actualStudents);
        }

        @Test
        @DisplayName("when DAOException from Dao should return DomainException")
        void  givenDaoExceprion_whenGetStudentsWithCourseName_thenThrowDomainException() throws DAOException {
            when(studentDaoMock.getStudentsWithCourseName(TEST_COURSE_NAME))
                    .thenThrow(DAOException.class);

            Exception exception = assertThrows(DomainException.class,
                    () -> service.getStudentsWithCourseName(TEST_COURSE_NAME));
            assertEquals(MESSAGE_GET_EXCEPTION, exception.getMessage());
        }

    }

    @Nested
    @DisplayName("test createStudent method")
    class testCreate {

        @Test
        @DisplayName("normal input should call studentDao.add method with right argument")
        void givenCreationOfStudent_whenAddStudent_thenCallStudentDaoAddMethodWithRightArgument() throws DAOException {
            service.createStudent(TEST_FIRST_NAME, TEST_LAST_NAME);
            verify(studentDaoMock, times(1)).add(student);
        }

        @Test
        @DisplayName("when DAOException from Dao should return DomainException")
        void  givenDaoExceprion_whenCreateStudent_thenThrowDomainException() throws DAOException {
            doThrow(DAOException.class).when(studentDaoMock).add(student);

            Exception exception = assertThrows(DomainException.class,
                    () -> service.createStudent(TEST_FIRST_NAME,
                            TEST_LAST_NAME));
            String expectedMessage = String.format(MESSAGE_CREATE_EXCEPTION,
                    student);
            assertEquals(expectedMessage, exception.getMessage());

        }

    }

    @Nested
    @DisplayName("test 'deleteById' method")
    class testDeleteById {

        @Test
        @DisplayName("method should call studentDao.delete with right parameter")
        void givenStudent_whenDeleteById_thenCallDaoWithParameter() throws DAOException {
            Student student = new Student(testStudentId);
            service.deleteById(testStudentId);

            verify(studentDaoMock, times(1)).delete(student);
        }

        @Test
        @DisplayName("when DAOException from Dao should return DomainException")
        void  givenMockedStudent_whenDeleteById_thenThrowDomainException() throws DAOException {
            doThrow(DAOException.class).when(studentDaoMock).delete(any());

            Exception exception = assertThrows(DomainException.class,
                    () -> service.deleteById(testStudentId));
            assertEquals(MESSAGE_DELETE_EXCEPTION, exception.getMessage());
        }

    }

    @Nested
    @DisplayName("test 'addStudentToCourse' method")
    class testAddStudentToCourse {

        @Test
        @DisplayName("method should call studentDao.addStudentCourse with right parameter")
        void  givenAddingStudentToCourse_whenAddStudentCourse_thenCallDaoWithParameter() throws DAOException {
            service.addStudentToCourse(testStudentId, testCourseId);
            verify(studentDaoMock, times(1)).addStudentCourse(testStudentId,
                    testCourseId);
        }

        @Test
        @DisplayName("when DAOException from Dao should return DomainException")
        void  givenMockedStudent_whenAddStudentCourse_thenThrowDomainException() throws DAOException {
            doThrow(DAOException.class).when(studentDaoMock)
                    .addStudentCourse(anyInt(), anyInt());

            Exception exception = assertThrows(DomainException.class,
                    () -> service.addStudentToCourse(testStudentId,
                            testCourseId));
            assertEquals(MESSAGE_ADD_STUDENT_COURSE_EXCEPTION,
                    exception.getMessage());
        }

    }

    @Nested
    @DisplayName("test 'removeStudentFromCourse' method")
    class testRemoveStudentFromCourse {

        @Test
        @DisplayName("method should call studentDao.removeStudentFromCourse with right parameter")
        void givenStudentInCourse_whenRemoveStudentFromCourse_thenCallDaoWithParameter() throws DAOException {
            service.removeStudentFromCourse(testStudentId, testCourseId);
            verify(studentDaoMock, times(1))
                    .removeStudentFromCourse(testStudentId, testCourseId);
        }

        @Test
        @DisplayName("when DAOException from Dao should return DomainException")
        void  givenMockedStudent_whenRemoveStudentFromCourse_thenThrowDomainException() throws DAOException {
            doThrow(DAOException.class).when(studentDaoMock)
                    .removeStudentFromCourse(anyInt(), anyInt());

            Exception exception = assertThrows(DomainException.class,
                    () -> service.removeStudentFromCourse(testStudentId,
                            testCourseId));
            String expectedMessage = String.format(
                    MESSAGE_DELETE_STUDENT_COURSE_EXCEPTION, testStudentId,
                    testCourseId);
            assertEquals(expectedMessage, exception.getMessage());
        }
    }
}
