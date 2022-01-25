package com.fox.dao;

import java.util.List;

import com.fox.entity.Course;
import com.fox.exception.DAOException;

public interface CourseDao extends Dao<Course> {

    List<Course> getCoursesForStudentId(int studentId) throws DAOException;

    List<Course> getCoursesMissingForStudentId(int studentId)
            throws DAOException;
}
