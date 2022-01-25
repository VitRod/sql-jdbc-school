package com.fox.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fox.dao.ConnectionProvider;
import com.fox.dao.SqlScriptRunner;
import com.fox.dao.jdbc.JdbcGroupDao;
import com.fox.entity.Group;
import com.fox.exception.DAOException;

class JdbcGroupDaoTest {
	
    private static final String FILENAME_STARTUP_SCRIPT = "test_schema.sql";
    private static final String FILENAME_FINISH_SCRIPT = "drop_all_tables.sql";
    private static final String NAME_GROUP_ID1 = "OR-41";
    private static final String NAME_GROUP_ID2 = "GM-87";
    private static final String NAME_GROUP_ID3 = "XI-12";
    private static final String TEST_GROUP_NAME = "TestGroup";

    private JdbcGroupDao groupDao;
    private Group groupId1;
    private Group groupId2;
    private Group groupId3;

    @BeforeEach
    void setUp() throws Exception {
        groupDao = new JdbcGroupDao();

        groupId1 = new Group(1, NAME_GROUP_ID1);
        groupId2 = new Group(2, NAME_GROUP_ID2);
        groupId3 = new Group(3, NAME_GROUP_ID3);

        try (Connection connection = ConnectionProvider.getConnection()) {
            SqlScriptRunner scriptRunner = new SqlScriptRunner(connection);
            scriptRunner.runSqlScript(FILENAME_STARTUP_SCRIPT);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (Connection connection = ConnectionProvider.getConnection()) {
            SqlScriptRunner scriptRunner = new SqlScriptRunner(connection);
            scriptRunner.runSqlScript(FILENAME_FINISH_SCRIPT);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

        @Test
        @DisplayName("add group should create new record in testDB with id=4")
        void givenGroup_whenAddGroup_thenReturnAddedGroup() throws DAOException {
            Group group = new Group(TEST_GROUP_NAME);
            groupDao.add(group);
            String expectedGroupName = group.getName();
            String actualGroupName = groupDao.getById(4).get().getName();
            assertEquals(expectedGroupName, actualGroupName);
        }

        @Test
        @DisplayName("get group by id=1 should return 'OR-41' group")
        void givenGroup_whenGetById_thenReturnGroupName() throws DAOException {
            String expectedGroupName = NAME_GROUP_ID1;
            String actualGroupName = groupDao.getById(1).get().getName();
            assertEquals(expectedGroupName, actualGroupName);
        }

        @Test
        @DisplayName("get group by non-existing id=6 should return empty Optional")
        void  givenGroups_whenGetGroupByNonExistingId_thenReturnEmptyOptional() throws DAOException {
            Optional<Group> expected = Optional.empty();
            Optional<Group> actual = groupDao.getById(6);
            assertEquals(expected, actual);
        }


        @Test
        @DisplayName("get all groups from base should return all groups")
        void givenGroups_whenGetAll_thenGetAllGroups() throws DAOException {
            List<Group> expectedGroups = new ArrayList<>();
            expectedGroups.add(groupId1);
            expectedGroups.add(groupId2);
            expectedGroups.add(groupId3);

            assertEquals(expectedGroups, groupDao.getAll());
        }

        @Test
        @DisplayName("update name group id=1 should write new name and getById(1) return new name")
        void  givenNewGroup_whenUpdateGroup_thenReturnNewGroupName() throws DAOException {
            Group newGroup = new Group(1, TEST_GROUP_NAME);
            groupDao.update(newGroup);
            String actualName = groupDao.getById(1).get().getName();
            assertEquals(TEST_GROUP_NAME, actualName);
        }


   
        @Test
        @DisplayName("delete group id=1 should delete group and getById(1) return empty Optional")
        void  givenGroup_whenDeleteGroupById_thenReturnEmptyOptional() throws DAOException {
            groupDao.delete(groupId1);
            Optional<Group> expected = Optional.empty();
            Optional<Group> actual = groupDao.getById(1);
            assertEquals(expected, actual);
        }
   

   
        @Test
        @DisplayName("get groups with less or equals 1 student should return such kind  of groups ")
        void  givenGroups_whenGetGroupsWithLessEqualsStudentCount_thenReturnGroupsLessEqualsOneStudent() throws DAOException {
            List<Group> expectedGroups = new ArrayList<>();
            expectedGroups.add(groupId1);
            expectedGroups.add(groupId2);
            expectedGroups.add(groupId3);

            List<Group> actualGroups = groupDao
                    .getGroupsWithLessEqualsStudentCount(1);
            assertEquals(expectedGroups, actualGroups);
        }

}
