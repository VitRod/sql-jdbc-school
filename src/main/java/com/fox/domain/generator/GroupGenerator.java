package com.fox.domain.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fox.entity.Group;

public class GroupGenerator implements Generator<Group> {
	
    private static final String FORMAT_MASK = "%s-%02d";
    private static final int NUMBER_CHAR_A = 65;
    private static final int NUMBER_CHAR_Z = 90;

    private Random random;

    public GroupGenerator(Random random) {
        this.random = random;
    }

    public List<Group> generate(int numberGroups) {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < numberGroups; i++) {
            Group group = new Group(generateName());
            groups.add(group);
        }
        return groups;
    }

    private String generateName() {
        int targetStringLength = 2;

        String leftPartName = random.ints(NUMBER_CHAR_A, NUMBER_CHAR_Z + 1)
                .limit(targetStringLength).collect(StringBuilder::new,
                        StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        int rightPartName = random.nextInt(101);

        return String.format(FORMAT_MASK, leftPartName, rightPartName);
    }
}
