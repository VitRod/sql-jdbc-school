package com.fox.domain;

import java.util.Random;

import com.fox.dao.ConnectionProvider;
import com.fox.dao.CourseDao;
import com.fox.dao.GroupDao;
import com.fox.dao.StartUpDao;
import com.fox.dao.StudentDao;
import com.fox.dao.jdbc.JdbcCourseDao;
import com.fox.dao.jdbc.JdbcGroupDao;
import com.fox.dao.jdbc.JdbcStudentDao;
import com.fox.domain.generator.CourseGenerator;
import com.fox.domain.generator.Generator;
import com.fox.domain.generator.GroupGenerator;
import com.fox.domain.generator.StudentGenerator;
import com.fox.domain.service.CourseService;
import com.fox.domain.service.GroupService;
import com.fox.domain.service.StudentService;
import com.fox.entity.Course;
import com.fox.entity.Group;
import com.fox.entity.Student;

public class Application {

    public static void main(String[] args) {

        StartUpDao startUpDao = new StartUpDao();
        Random random = new Random();

        GroupDao groupDao = new JdbcGroupDao();
        Generator<Group> groupGenerator = new GroupGenerator(random);
        GroupService groupService = new GroupService(groupDao, groupGenerator);

        CourseDao courseDao = new JdbcCourseDao();
        Generator<Course> courseGenerator = new CourseGenerator();
        CourseService courseService = new CourseService(courseDao,
                courseGenerator);

        StudentDao studentDao = new JdbcStudentDao();
        Generator<Student> studentGenerator = new StudentGenerator(random);
        StudentService studentService = new StudentService(studentDao,
                studentGenerator, random);


        Faсade facade = new Faсade(startUpDao, groupService,
                courseService, studentService);
        
        facade.prepareBase();
        facade.workWithBase();

    }

}
