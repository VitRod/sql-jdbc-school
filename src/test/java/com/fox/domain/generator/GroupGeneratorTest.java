package com.fox.domain.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fox.entity.Group;
import com.fox.exception.DAOException;

@ExtendWith(MockitoExtension.class)
class GroupGeneratorTest {
	
    private static final String TEST_GROUP_NAME = "-00";
    private GroupGenerator groupGenerator;

    @Mock
    Random random;

    @BeforeEach
    void setUp() throws Exception {
        groupGenerator = new GroupGenerator(random);
    }

    @Test
    @DisplayName("test generate with numberGroups = 1 should return List groups from 1 group and verify calls to the group")
    void givenTest_whenGenerateGroup_thenReturnGroup() throws DAOException {
        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(new Group(0, TEST_GROUP_NAME));

        List<Group> actualGroups = groupGenerator.generate(1);

        assertEquals(expectedGroups, actualGroups);

    }

}
