package com.fox.domain;

import com.fox.cli.MenuStarter;
import com.fox.dao.StartUpDao;
import com.fox.domain.service.CourseService;
import com.fox.domain.service.GroupService;
import com.fox.domain.service.StudentService;
import com.fox.exception.DAOException;
import com.fox.exception.DomainException;

public class Faсade {
	
    private static final String MESSAGE_START_PREPARE = "Starting to prepare the base. It can take some time ...";
    private static final String MESSAGE_FINISH_PREPARE = "Database prepared";
    private static final int NUMBER_STUDENTS = 200;
    private static final int NUMBER_COURSES = 10;
    private static final int NUMBER_GROUPS = 10;
    private static final String MESSAGE_EXCEPTION_CREATE_TABLES = "Can't delete and create tables";

    private StartUpDao startUpDao;
    private GroupService groupService;
    private CourseService courseService;
    private StudentService studentService;

    public Faсade(StartUpDao startUpDao, GroupService groupService,
            CourseService courseService, StudentService studentService) {
        this.startUpDao = startUpDao;
        this.groupService = groupService;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    public void prepareBase() throws DomainException {
        System.out.println(MESSAGE_START_PREPARE);
        createTables();
        fillTables();
        System.out.println(MESSAGE_FINISH_PREPARE);
    }

    public void workWithBase() {
        MenuStarter menu = new MenuStarter(groupService, studentService,
                courseService);
        menu.startMenu();
    }

    private void createTables() {
        try {
            startUpDao.prepareTables();
        } catch (DAOException e) {
            throw new DomainException(MESSAGE_EXCEPTION_CREATE_TABLES, e);
        }
    }

    private void fillTables() {
        groupService.createTestGroups(NUMBER_GROUPS);
        courseService.createTestCourses(NUMBER_COURSES);
        studentService.createTestStudents(NUMBER_STUDENTS);
        studentService.createTestStudentsCourses(NUMBER_STUDENTS);
    }

}
