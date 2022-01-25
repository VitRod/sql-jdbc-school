package com.fox.cli.menuitem;

import java.util.List;
import java.util.Scanner;

import com.fox.domain.service.GroupService;
import com.fox.entity.Group;

public class FindGroupsLessStudentsCountMenuItem extends MenuItem {
	
    private static final String MESSAGE_INPUT_STUDENT_COUNT = "Input student count: ";

    private Scanner scanner;
    private GroupService service;

    public FindGroupsLessStudentsCountMenuItem(String name,
            GroupService service, Scanner scanner) {
        super(name);
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        int studentCount = inputStudentCount();
        List<Group> groups = service
                .getGroupsWithLessEqualsStudentCount(studentCount);
        groups.stream().forEach(System.out::println);
    }

    private int inputStudentCount() {
        int studentCount = 0;
        System.out.print(MESSAGE_INPUT_STUDENT_COUNT);
        while (scanner.hasNext()) {
            studentCount = Integer.parseInt(scanner.nextLine());
            break;
        }
        return studentCount;
    }
}
