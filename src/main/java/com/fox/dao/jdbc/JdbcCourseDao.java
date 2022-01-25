package com.fox.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import com.fox.dao.ConnectionProvider;
import com.fox.dao.CourseDao;
import com.fox.entity.Course;
import com.fox.exception.DAOException;
import com.fox.reader.Reader;

public class JdbcCourseDao implements CourseDao {
	
    private static final String PROPERTY_COURSE_ADD = "INSERT INTO courses(course_name, course_description) VALUES (?, ?)";
    
    private static final String PROPERTY_COURSE_GET_BY_ID = "SELECT course_id, course_name, course_description FROM courses WHERE course_id = ?";
   
    private static final String PROPERTY_COURSE_GET_ALL = "SELECT course_id, course_name, course_description FROM courses";
    
    private static final String PROPERTY_COURSE_UPDATE = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?";
    
    private static final String PROPERTY_COURSE_DELETE = "DELETE FROM courses WHERE course_id = ?";
    
    private static final String PROPERTY_COURSE_GET_FOR_STUDENT =
    		"SELECT courses.course_id, courses.course_name, courses.course_description " +
    		"FROM courses " + 
    		"INNER JOIN students_courses ON courses.course_id = students_courses.course_id " + 
    		"WHERE students_courses.student_id = ?";
    
    private static final String PROPERTY_COURSE_GET_MISS_FOR_STUDENT = 
    		"SELECT course_id, course_name, course_description " + 
    		"FROM courses c " + 
    		"WHERE NOT EXISTS (SELECT * FROM students_courses s_c WHERE student_id = ? " +
    		"AND c.course_id = s_c.course_id)";
    
    private static final String FIELD_COURSE_ID = "course_id";
    private static final String FIELD_COURSE_NAME = "course_name";
    private static final String FIELD_COURSE_DESCRIPTION = "course_description";
    private static final String MESSAGE_EXCEPTION_ADD = "Can't add course";
    private static final String MESSAGE_EXCEPTION_GET_BY_ID = "Can't get course by ID = ";
    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get courses";
    private static final String MESSAGE_EXCEPTION_UPDATE = "Can't update course ";
    private static final String MESSAGE_EXCEPTION_DELETE = "Can't delete course ";

    @Override
    public void add(Course course) throws DAOException {
        try (Connection connection = ConnectionProvider.getConnection()) {

            try (PreparedStatement statement = connection
                    .prepareStatement(PROPERTY_COURSE_ADD)) {
                statement.setString(1, course.getName());
                statement.setString(2, course.getDescription());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_ADD, e);
        }
    }

    @Override
    public Optional<Course> getById(int courseId) throws DAOException {
        Course course = null;
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_COURSE_GET_BY_ID)) {
            statement.setInt(1, courseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    course = new Course();
                    course.setId(courseId);
                    course.setName(
                            resultSet.getString(FIELD_COURSE_NAME));
                    course.setDescription(
                            resultSet.getString(FIELD_COURSE_DESCRIPTION));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_GET_BY_ID + courseId, e);
        }
        return Optional.ofNullable(course);
    }

    @Override
    public List<Course> getAll() throws DAOException {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = ConnectionProvider.getConnection();
                Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(PROPERTY_COURSE_GET_ALL)) {
                while (resultSet.next()) {
                    Course course = new Course();
                    course.setId(resultSet.getInt(FIELD_COURSE_ID));
                    course.setName(
                            resultSet.getString(FIELD_COURSE_NAME));
                    course.setDescription(
                            resultSet.getString(FIELD_COURSE_DESCRIPTION));

                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_GET_ALL, e);
        }
        return courses;
    }

    @Override
    public void update(Course course) throws DAOException {
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_COURSE_UPDATE)) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(
                    MESSAGE_EXCEPTION_UPDATE + course.getId(), e);
        }
    }

    @Override
    public void delete(Course course) throws DAOException {
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_COURSE_DELETE)) {
            statement.setInt(1, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(
                    MESSAGE_EXCEPTION_DELETE + course.getId(), e);
        }
    }
    @Override
    public List<Course> getCoursesForStudentId(int studentId) throws DAOException {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(PROPERTY_COURSE_GET_FOR_STUDENT)){
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Course course = new Course();
                    course.setId(resultSet.getInt(FIELD_COURSE_ID));
                    course.setName(
                            resultSet.getString(FIELD_COURSE_NAME));
                    course.setDescription(
                            resultSet.getString(FIELD_COURSE_DESCRIPTION));

                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_GET_ALL, e);
        }
        return courses;
        
    }
    
    @Override
    public List<Course> getCoursesMissingForStudentId(int studentId) throws DAOException { 
        List<Course> courses = new ArrayList<>();
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(PROPERTY_COURSE_GET_MISS_FOR_STUDENT)){
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Course course = new Course();
                    course.setId(resultSet.getInt(FIELD_COURSE_ID));
                    course.setName(
                            resultSet.getString(FIELD_COURSE_NAME));
                    course.setDescription(
                            resultSet.getString(FIELD_COURSE_DESCRIPTION));

                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_GET_ALL, e);
        }
        return courses;
        
    }
}
