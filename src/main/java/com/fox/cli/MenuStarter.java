package com.fox.cli;

import java.util.Scanner;

import com.fox.cli.menuitem.AddNewStudentMenuItem;
import com.fox.cli.menuitem.AddStudentToCourseMenuItem;
import com.fox.cli.menuitem.DeleteStudentByIdMenuItem;
import com.fox.cli.menuitem.FindGroupsLessStudentsCountMenuItem;
import com.fox.cli.menuitem.GetStudentsWithCourseNameMenuItem;
import com.fox.cli.menuitem.RemoveStudentFromCourseMenuItem;
import com.fox.domain.service.CourseService;
import com.fox.domain.service.GroupService;
import com.fox.domain.service.StudentService;

public class MenuStarter {
	
    private GroupService groupService;
    private StudentService studentService;
    private CourseService courseService;

    public MenuStarter(GroupService groupService, StudentService studentService,
            CourseService courseService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public void startMenu() {
        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu(scanner);

        FindGroupsLessStudentsCountMenuItem menuItem1 = new FindGroupsLessStudentsCountMenuItem(
                "Find all groups with less or equals student count",
                groupService, scanner);
        menu.addMenuItem(1, menuItem1);

        GetStudentsWithCourseNameMenuItem menuItem2 = new GetStudentsWithCourseNameMenuItem(
                "Find all students related to course with given name",
                studentService, scanner);
        menu.addMenuItem(2, menuItem2);

        AddNewStudentMenuItem menuItem3 = new AddNewStudentMenuItem(
                "Add new student", studentService, scanner);
        menu.addMenuItem(3, menuItem3);

        DeleteStudentByIdMenuItem menuItem4 = new DeleteStudentByIdMenuItem(
                "Delete student by STUDENT_ID", studentService, scanner);
        menu.addMenuItem(4, menuItem4);

        AddStudentToCourseMenuItem menuItem5 = new AddStudentToCourseMenuItem(
                "Add a student to the course (from a list)", courseService,
                studentService, scanner);
        menu.addMenuItem(5, menuItem5);

        RemoveStudentFromCourseMenuItem menuItem6 = new RemoveStudentFromCourseMenuItem(
                "Remove the student from one of his or her courses",
                courseService, studentService, scanner);
        menu.addMenuItem(6, menuItem6);

        menu.initMenu();
        scanner.close();
    }

}
