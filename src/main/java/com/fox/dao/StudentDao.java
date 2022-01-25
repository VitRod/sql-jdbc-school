package com.fox.dao;

import java.util.List;

import com.fox.entity.Student;
import com.fox.exception.DAOException;

public interface StudentDao extends Dao<Student> {

    void addStudentCourse(int studentId, int courseId) throws DAOException;

    void removeStudentFromCourse(int studentId, int courseId)
            throws DAOException;

    List<Student> getStudentsWithCourseName(String courseName)
            throws DAOException;

}
