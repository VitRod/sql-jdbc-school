package com.fox.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import com.fox.dao.ConnectionProvider;
import com.fox.dao.StudentDao;
import com.fox.entity.Student;
import com.fox.exception.DAOException;
import com.fox.reader.Reader;

public class JdbcStudentDao implements StudentDao {
	
    private static final String PROPERTY_STUDENT_ADD = "INSERT INTO students(group_id, first_name, last_name) VALUES (?, ?, ?)";
    
    private static final String PROPERTY_STUDENT_GET_BY_ID = "SELECT student_id, group_id, first_name, last_name FROM students WHERE student_id = ?";
       
    private static final String PROPERTY_STUDENT_GET_ALL = "SELECT student_id, group_id, first_name, last_name FROM students";
    
    private static final String PROPERTY_STUDENT_UPDATE = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    
    private static final String PROPERTY_STUDENT_DELETE = "DELETE FROM students WHERE student_id = ?";
    
    private static final String PROPERTY_STUDENT_GET_WITH_COURSE_NAME = 
    		"SELECT students.student_id, students.group_id, students.first_name, students.last_name " + 
    		"FROM students " + 
    		"INNER JOIN (students_courses INNER JOIN courses USING (course_id)) " + 
    		"USING (student_id) " +
    		"WHERE courses.course_name = ? ";
    
    private static final String PROPERTY_STUDENT_COURSE_ADD = "INSERT INTO students_courses(student_id, course_id) VALUES (?, ?)";
   
    private static final String PROPERTY_STUDENT_COURSE_DELETE = "DELETE FROM students_courses WHERE student_id = ? and course_id = ?";
    
    private static final String FIELD_STUDENT_ID = "student_id";
    private static final String FIELD_GROUP_ID = "group_id";
    private static final String FIELD_FIRST_NAME = "first_name";
    private static final String FIELD_LAST_NAME = "last_name";
    private static final String MESSAGE_EXCEPTION_ADD = "Can't add student";
    private static final String MESSAGE_EXCEPTION_GET_BY_ID = "Can't get student by ID = ";
    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get students";
    private static final String MESSAGE_EXCEPTION_UPDATE = "Can't update student ";
    private static final String MESSAGE_EXCEPTION_DELETE = "Can't delete student ";

    private static final String MESSAGE_EXCEPTION_ADD_STUDENT_COURSE = "Can't add pair student-course";
    private static final String MESSAGE_EXCEPTION_DELETE_STUDENT_COURSE = "Can't delete student %d from course %d";

    @Override
    public void add(Student student) throws DAOException {   
        try (Connection connection = ConnectionProvider.getConnection()) {

            try (PreparedStatement statement = connection
                    .prepareStatement(PROPERTY_STUDENT_ADD)) {
                if (student.getGroupId() != 0) {
                    statement.setInt(1, student.getGroupId());
                } else {
                    statement.setNull(1, Types.INTEGER);
                }
                statement.setString(2, student.getFirstName());
                statement.setString(3, student.getLastName());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_ADD, e);
        }

    }

    @Override
    public Optional<Student> getById(int studentId) throws DAOException {      
        Student student = null;
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_STUDENT_GET_BY_ID)) {
            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    student = new Student();
                    student.setId(studentId);
                    student.setGroupId(resultSet.getInt(FIELD_GROUP_ID));
                    student.setFirstName(resultSet.getString(FIELD_FIRST_NAME));
                    student.setLastName(resultSet.getString(FIELD_LAST_NAME));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_GET_BY_ID + studentId, e);
        }
        return Optional.ofNullable(student);
    }

    @Override
    public List<Student> getAll() throws DAOException {      
        List<Student> students = new ArrayList<>();
        try (Connection connection = ConnectionProvider.getConnection();
                Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(PROPERTY_STUDENT_GET_ALL)) {
                while (resultSet.next()) {
                    Student student = new Student();
                    student.setId(resultSet.getInt(FIELD_STUDENT_ID));
                    student.setGroupId(resultSet.getInt(FIELD_GROUP_ID));
                    student.setFirstName(resultSet.getString(FIELD_FIRST_NAME));
                    student.setLastName(resultSet.getString(FIELD_LAST_NAME));

                    students.add(student);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_GET_ALL, e);
        }
        return students;
    }

    @Override
    public void update(Student student) throws DAOException { 
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_STUDENT_UPDATE)) {
            if (student.getGroupId() != 0) {
                statement.setInt(1, student.getGroupId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            statement.setInt(4, student.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(
                    MESSAGE_EXCEPTION_UPDATE + student.getId(), e);
        }
    }

    @Override
    public void delete(Student student) throws DAOException {    
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_STUDENT_DELETE)) {
            statement.setInt(1, student.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(
                    MESSAGE_EXCEPTION_DELETE + student.getId(), e);
        }
    }
    
    
    @Override
    public void addStudentCourse(int studentId, int courseId)
            throws DAOException {     
        try (Connection connection = ConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection
                    .prepareStatement(PROPERTY_STUDENT_COURSE_ADD)) {
                statement.setInt(1, studentId);
                statement.setInt(2, courseId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_ADD_STUDENT_COURSE, e);
        }

    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId)
            throws DAOException {     
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_STUDENT_COURSE_DELETE)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(
                    String.format(MESSAGE_EXCEPTION_DELETE_STUDENT_COURSE,
                            studentId, courseId),
                    e);
        }
    }

    @Override
    public List<Student> getStudentsWithCourseName(String courseName)
            throws DAOException {      
        List<Student> students = new ArrayList<>();
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_STUDENT_GET_WITH_COURSE_NAME)) {
            statement.setString(1, courseName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = new Student();
                    student.setId(resultSet.getInt(FIELD_STUDENT_ID));
                    student.setGroupId(resultSet.getInt(FIELD_GROUP_ID));
                    student.setFirstName(resultSet.getString(FIELD_FIRST_NAME));
                    student.setLastName(resultSet.getString(FIELD_LAST_NAME));

                    students.add(student);
                }
            }

        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_GET_ALL, e);
        }
        return students;
    }
}
