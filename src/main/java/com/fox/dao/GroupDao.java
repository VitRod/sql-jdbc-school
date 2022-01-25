package com.fox.dao;

import java.util.List;

import com.fox.entity.Group;
import com.fox.exception.DAOException;

public interface GroupDao extends Dao<Group> {

    List<Group> getGroupsWithLessEqualsStudentCount(int studentCount) throws DAOException;

}
