package com.fox.cli.menuitem;

import java.util.List;
import java.util.Scanner;

import com.fox.domain.service.StudentService;
import com.fox.entity.Student;

public class GetStudentsWithCourseNameMenuItem extends MenuItem {
	
    private static final String MESSAGE_INPUT_COURSE_NAME = "Input course name: ";
    private StudentService service;
    private Scanner scanner;

    public GetStudentsWithCourseNameMenuItem(String name,
            StudentService service, Scanner scanner) {
        super(name);
        this.scanner = scanner;
        this.service = service;
    }

    @Override
    public void execute() {
        System.out.print(MESSAGE_INPUT_COURSE_NAME);
        String courseName = scanner.nextLine();
        List<Student> students = service.getStudentsWithCourseName(courseName);
        students.stream().forEach(System.out::println);
    }
}
