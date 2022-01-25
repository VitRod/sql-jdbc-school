package com.fox.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fox.dao.jdbc.JdbcCourseDao;
import com.fox.domain.generator.Generator;
import com.fox.domain.service.CourseService;
import com.fox.entity.Course;
import com.fox.exception.DAOException;
import com.fox.exception.DomainException;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
	
    private static final int STUDENT_ID_EXCEPTION = 6;
    private static final String MESSAGE_EXCEPTION = "Can't get courses";

    private CourseService service;
    private List<Course> courses;
    private Course course;

    @Mock
    private JdbcCourseDao courseDaoMock;

    @Mock
    private Generator<Course> generatorMock;

    @BeforeEach
    void setUp() throws Exception {
        service = new CourseService(courseDaoMock, generatorMock);
        courses = new ArrayList<>();
    }

    @Nested
    @DisplayName("test 'createTestCourses' method")
    class testCreateTestCourses {
        private int numberCourses = 5;

        @Test
        @DisplayName("method should call generator.generate once")
        void givenCreationOfTestCourses_whenGenerate_thenMethodGenerateIsCalledOnce() throws DAOException {
            service.createTestCourses(numberCourses);

            verify(generatorMock, times(1)).generate(numberCourses);
        }

        @Test
        @DisplayName("method should call courseDao as much time as NumberCourses")        
        void givenAddedCourses_whenGenerateNumberCourses_thenCourseDaoIsCalledAsMuchTimeAsNumberCourses() throws DAOException {

            for (int i = 0; i < numberCourses; i++) {
                courses.add(course);
            }
            when(generatorMock.generate(numberCourses)).thenReturn(courses);

            service.createTestCourses(numberCourses);

            verify(courseDaoMock, times(numberCourses)).add(course);
        }
    }

    @Nested
    @DisplayName("test getCoursesMissingForStudent method")
    class testGetCoursesMissingForStudent {

        @Test
        void givenMockedCourses_whenGetCoursesMissingForStudent_thenReturnListCourses() throws DAOException {
            when(courseDaoMock.getCoursesMissingForStudentId(anyInt()))
                    .thenReturn(courses);

            List<Course> actualCourses = service
                    .getCoursesMissingForStudent(anyInt());

            assertEquals(courses, actualCourses);
        }

        @Test
        void givenMockedCourses_whenGetCoursesMissingForStudentId_thenThrowDomainException() throws DAOException {
            when(courseDaoMock
                    .getCoursesMissingForStudentId(STUDENT_ID_EXCEPTION))
                            .thenThrow(DAOException.class);

            Exception exception = assertThrows(DomainException.class,
                    () -> service
                            .getCoursesMissingForStudent(STUDENT_ID_EXCEPTION));
            assertEquals(MESSAGE_EXCEPTION, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("test 'getCoursesForStudent' method")
    class testGetCoursesForStudent {

        @Test
        void givenMockedCourses_whenGetCoursesForStudent_thenReturnListCourses() throws DAOException {
            when(courseDaoMock.getCoursesForStudentId(anyInt()))
                    .thenReturn(courses);

            List<Course> actualCourses = service.getCoursesForStudent(anyInt());

            assertEquals(courses, actualCourses);
        }

        @Test
        void givenMockedCourses_whenGetCoursesForStudentId_thenThrowDomainException() throws DAOException {
            when(courseDaoMock.getCoursesForStudentId(STUDENT_ID_EXCEPTION))
                    .thenThrow(DAOException.class);

            Exception exception = assertThrows(DomainException.class,
                    () -> service.getCoursesForStudent(STUDENT_ID_EXCEPTION));
            assertEquals(MESSAGE_EXCEPTION, exception.getMessage());
        }

    }

}
