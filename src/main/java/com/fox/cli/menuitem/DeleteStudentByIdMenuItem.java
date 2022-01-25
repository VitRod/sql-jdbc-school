package com.fox.cli.menuitem;

import java.util.Scanner;

import com.fox.domain.service.StudentService;

public class DeleteStudentByIdMenuItem extends MenuItem {
	
    private static final String MESSAGE_INPUT_STUDENT_ID = "Input student_id for deleting: ";
    private static final String MASK_MESSAGE_DELETE_STUDENT = "Student with id %d is deleted";
    private static final String MESSAGE_EXCEPTION_NOT_NUMBER = "You inputted not a number. Please input number ";
    
    private StudentService service;
    private Scanner scanner;

    public DeleteStudentByIdMenuItem(String name, StudentService service,
            Scanner scanner) {
        super(name);
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        int studentId = inputStudentId();
        service.deleteById(studentId);
        System.out
                .println(String.format(MASK_MESSAGE_DELETE_STUDENT, studentId));
    }

    private int inputStudentId() {
        int result = -1;
        do{
            System.out.print(MESSAGE_INPUT_STUDENT_ID);
            try {
                result = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print(MESSAGE_EXCEPTION_NOT_NUMBER);
            }
        } while(result == -1);
        return result;
    }
}
