package com.fox.reader;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fox.reader.Reader;

class ReaderTest {
	
    private static final String MESSAGE_FILENAME_IS_NULL = "Filename is null";
    private static final String FILENAME_MISSING_FILE = "missing file";
    private static final String MESSAGE_EXCEPTION_FILE_NOT_FOUND = "File \"missing file\" not found";
    private static final String FILENAME_PROPERTY_FILE = "db.properties";
    private static final String PROPERTY_DB_LOGIN = "db.login";
    private static final String EXPECTED_LOGIN = "sa";

    private Reader reader;

    @BeforeEach
    void setUp() throws Exception {
        reader = new Reader();
    }

    @Nested
    @DisplayName("test readProperties method")
    class testReadPropperties {

        @Test
        @DisplayName("test null input string have to return IllegalArgumentException")
        void  givenNullInputString_whenReadProperties_thenReturnIllegalArgumentException () {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> reader.readProperties(null));
            assertEquals(MESSAGE_FILENAME_IS_NULL, exception.getMessage());
        }

        @Test
        @DisplayName("test input missing file have to return IllegalArgumentException")
        void  givenInputMissingFile_whenReadProperties_thenReturnIllegalArgumentException () {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> reader.readProperties(FILENAME_MISSING_FILE));
            assertEquals(MESSAGE_EXCEPTION_FILE_NOT_FOUND,
                    exception.getMessage());
        }

        @Test
        @DisplayName("test normal input propertyFile have to return expected values of properties")
        void  givenNormalInputPropertyFile_whenReadProperties_thenReturnValuesOfProperties() {
            Properties propDb = reader.readProperties(FILENAME_PROPERTY_FILE);
            String expectedDbLogin = EXPECTED_LOGIN;
            String actualDbLogin = propDb.getProperty(PROPERTY_DB_LOGIN);
            assertEquals(expectedDbLogin, actualDbLogin);
        }
    }

    @Nested
    @DisplayName("test readTxtDataFiles method")
    class testreadTxtDataFiles {
        private static final String FILENAME_STUDENTS_FIRST_NAMES = "student_first_names.txt";
        private static final String FIRST_NAME_1 = "Sirko";
        private static final String FIRST_NAME_2 = "Peter";
        private static final String FIRST_NAME_3 = "John";

        @Test
        @DisplayName("test null input string have to return IllegalArgumentException")
        void  givenNullInputString_whenReadTxtDataFiles_thenReturnIllegalArgumentException () {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> reader.readTxtDataFiles(null));
            assertEquals(MESSAGE_FILENAME_IS_NULL, exception.getMessage());
        }

        @Test
        @DisplayName("test input missing file have to return IllegalArgumentException")
        void  givenInputMissingFile_whenReadTxtDataFiles_thenReturnIllegalArgumentException () {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> reader.readTxtDataFiles(FILENAME_MISSING_FILE));
            assertEquals(MESSAGE_EXCEPTION_FILE_NOT_FOUND,
                    exception.getMessage());
        }

        @Test
        @DisplayName("test normal input student_first_names have to return expected List<String>")
        void  givenNormalInputTxtFile_readTxtDataFiles_thenReturnExpectedList() {
            String[] firstNames = {FIRST_NAME_1, FIRST_NAME_2, FIRST_NAME_3};
            List<String> expectedFirstNames = Arrays.asList(firstNames);
            List<String> actualFirstNames = reader.readTxtDataFiles(FILENAME_STUDENTS_FIRST_NAMES);
            assertEquals(expectedFirstNames, actualFirstNames);
        }
    }
}
