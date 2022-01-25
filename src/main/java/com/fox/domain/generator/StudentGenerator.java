package com.fox.domain.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fox.entity.Student;
import com.fox.reader.Reader;

public class StudentGenerator implements Generator<Student> {
	
    private static final int MIN_GROUP_SIZE = 19;
    private static final int MAX_GROUP_SIZE = 30;
    private static final String FILENAME_FIRST_NAME_DATA = "student_first_names.txt";
    private static final String FILENAME_LAST_NAME_DATA = "student_last_names.txt";
    private Random random;

    public StudentGenerator(Random random) {
        this.random = random;
    }

    public List<Student> generate(int numberStudents) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < numberStudents; i++) {
            String[] names = createStudentNames();
            Student student = new Student(names[0], names[1]);
            students.add(student);
        }
        return splitStudentsToGroups(students);
    }

    private String[] createStudentNames() {
        Reader reader = new Reader();
        List<String> firstNames = reader
                .readTxtDataFiles(FILENAME_FIRST_NAME_DATA);
        List<String> lastNames = reader
                .readTxtDataFiles(FILENAME_LAST_NAME_DATA);

        String[] studentNames = new String[2];
        studentNames[0] = firstNames.get(random.nextInt(20));
        studentNames[1] = lastNames.get(random.nextInt(20));

        return studentNames;
    }

    private List<Student> splitStudentsToGroups(List<Student> students) {
        int numberStudents = students.size();
        int[] sizeGroups = calculateSizeGroups(numberStudents);

        for (Student student : students) {
            for (int i = 0; i < sizeGroups.length; i++) {
                if (sizeGroups[i] != 0) {
                    student.setGroupId(i + 1);
                    sizeGroups[i]--;
                    break;
                }
            }
        }

        return students;
    }

    private int[] calculateSizeGroups(int numberStudents) {
        int[] numberStudentInGroups = new int[10];
        List<Integer> variantSizes = Stream.iterate(0, n -> n + 1)
                .limit(MAX_GROUP_SIZE + 1l)
                .filter(n -> n == 0 || n > MIN_GROUP_SIZE)
                .collect(Collectors.toList());
        for (int i = 0; i < numberStudentInGroups.length; i++) {
            if (numberStudents > 10) {
                numberStudentInGroups[i] = variantSizes
                        .get(random.nextInt(variantSizes.size()));
            } else {
                numberStudentInGroups[i] = 0;
            }
            numberStudents -= numberStudentInGroups[i];
        }
        return numberStudentInGroups;
    }

}
