package com.fox.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import com.fox.dao.ConnectionProvider;
import com.fox.dao.GroupDao;
import com.fox.entity.Group;
import com.fox.exception.DAOException;
import com.fox.reader.Reader;

public class JdbcGroupDao implements GroupDao {

    private static final String PROPERTY_GROUP_ADD = "INSERT INTO groups(group_name) VALUES (?)";
    
    private static final String PROPERTY_GROUP_GET_BY_ID = "SELECT group_name FROM groups WHERE group_id = ?";
    
    private static final String PROPERTY_GROUP_GET_ALL = "SELECT group_id, group_name FROM groups";
    
    private static final String PROPERTY_GROUP_UPDATE = "UPDATE groups SET group_name = ? WHERE group_id = ?";
    
    private static final String PROPERTY_GROUP_DELETE = "DELETE FROM groups WHERE group_id = ?";
    
    private static final String PROPERTY_FIND_GROUPS_LESS_STUDENT_COUNT = 
    		"SELECT groups.group_id, groups.group_name, COUNT(student_id) " + 
    		"FROM groups " + 
    		"LEFT JOIN students ON groups.group_id = students.group_id " + 
    		"GROUP BY groups.group_id, groups.group_name " + 
    		"HAVING COUNT(student_id) <= ?";
     
    private static final String FIELD_GROUP_ID = "group_id";
    private static final String FIELD_GROUP_NAME = "group_name";
    private static final String MESSAGE_EXCEPTION_ADD = "Can't add group";
    private static final String MESSAGE_EXCEPTION_GET_BY_ID = "Can't get group by ID = ";
    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get groups";
    private static final String MESSAGE_EXCEPTION_GROUP_UPDATE = "Can't update group ";
    private static final String MESSAGE_EXCEPTION_GROUP_DELETE = "Can't delete group ";

    @Override
    public void add(Group group) throws DAOException {
        try (Connection connection = ConnectionProvider.getConnection()) {

            try (PreparedStatement statement = connection
                    .prepareStatement(PROPERTY_GROUP_ADD)) {
                statement.setString(1, group.getName());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_ADD, e);
        }

    }

    @Override
    public Optional<Group> getById(int groupId) throws DAOException {  
        Group group = null;
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_GROUP_GET_BY_ID)) {
            statement.setInt(1, groupId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    group = new Group(groupId,
                            resultSet.getString(FIELD_GROUP_NAME));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_GET_BY_ID + groupId, e);
        }
        return Optional.ofNullable(group);
    }

    @Override
    public List<Group> getAll() throws DAOException {
        List<Group> groups = new ArrayList<>();
        try (Connection connection = ConnectionProvider.getConnection();
                Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(PROPERTY_GROUP_GET_ALL)) {
                while (resultSet.next()) {
                    Group group = new Group(resultSet.getInt(FIELD_GROUP_ID),
                            resultSet.getString(FIELD_GROUP_NAME));
                    groups.add(group);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_GET_ALL, e);
        }
        return groups;
    }

    @Override
    public void update(Group group) throws DAOException {
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_GROUP_UPDATE)) {
            statement.setString(1, group.getName());
            statement.setInt(2, group.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(
                    MESSAGE_EXCEPTION_GROUP_UPDATE + group.getId(), e);
        }

    }

    @Override
    public void delete(Group group) throws DAOException {      
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(PROPERTY_GROUP_DELETE)) {
            statement.setInt(1, group.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(
                    MESSAGE_EXCEPTION_GROUP_DELETE + group.getId(), e);
        }

    }

    @Override
    public List<Group> getGroupsWithLessEqualsStudentCount(int studentCount)
            throws DAOException {
        List<Group> groups = new ArrayList<>();
        try (Connection connection = ConnectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(PROPERTY_FIND_GROUPS_LESS_STUDENT_COUNT)) {
            statement.setInt(1, studentCount);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Group group = new Group(resultSet.getInt(FIELD_GROUP_ID),
                            resultSet.getString(FIELD_GROUP_NAME));
                    groups.add(group);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MESSAGE_EXCEPTION_GET_ALL, e);
        }
        return groups;
    }

}
