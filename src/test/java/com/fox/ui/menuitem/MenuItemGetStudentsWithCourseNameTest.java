package com.fox.ui.menuitem;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fox.cli.menuitem.GetStudentsWithCourseNameMenuItem;
import com.fox.domain.service.StudentService;

@ExtendWith(MockitoExtension.class)
class MenuItemGetStudentsWithCourseNameTest {
	
    private static final String COURSE_NAME = "COURSE_NAME";

    private static final String NAME_MENU = "Name Menu";
    
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;
    
    private GetStudentsWithCourseNameMenuItem item;
    private Scanner scanner;
    
    @Mock
    StudentService studentService;

    @BeforeEach
    void setUpOutput() throws Exception {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void restoreSystemInputOutput() throws Exception {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    @DisplayName("test verify call services from arguments")
    void givenCourseName_whenGetStudentsWithCourseName_thenVerifyCallServicesOneTimeFromArgument() {
        String input = COURSE_NAME;
        provideInput(input);
        scanner = new Scanner(testIn);
        item = new GetStudentsWithCourseNameMenuItem(NAME_MENU, studentService, scanner);
        item.execute();
        verify(studentService, times(1)).getStudentsWithCourseName(COURSE_NAME);

    }
}
