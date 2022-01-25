package com.fox.cli.menuitem;

import java.util.Scanner;

import com.fox.domain.service.StudentService;

public class AddNewStudentMenuItem extends MenuItem {
	
    private static final String MESSAGE_FIRST_NAME = "Input first name: ";
    private static final String MESSAGE_LAST_NAME = "Input last name: ";
    private static final String MASK_MESSAGE_ADD_STUDENT = "Student %s %s is created";

    private StudentService service;
    private Scanner scanner;

    public AddNewStudentMenuItem(String name, StudentService service,
            Scanner scanner) {
        super(name);
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print(MESSAGE_FIRST_NAME);
        String firstName = scanner.nextLine();
        System.out.print(MESSAGE_LAST_NAME);
        String lastName = scanner.nextLine();
        service.createStudent(firstName, lastName);
        System.out.println(String.format(MASK_MESSAGE_ADD_STUDENT,
                firstName, lastName));
    }
}
