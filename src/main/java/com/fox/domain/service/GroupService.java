package com.fox.domain.service;

import java.util.List;

import com.fox.dao.GroupDao;
import com.fox.domain.generator.Generator;
import com.fox.entity.Group;
import com.fox.exception.DAOException;
import com.fox.exception.DomainException;

public class GroupService {
	
    private static final String MASK_MESSAGE_ADD_EXCEPTION = "Don't save group %s in base";
    private static final String MESSAGE_GET_EXCEPTION = "Can't get groups";

    private GroupDao groupDao;
    private Generator<Group> generator;

    public GroupService(GroupDao groupDao, Generator<Group> generator) {
        this.groupDao = groupDao;
        this.generator = generator;
    }

    public void createTestGroups(int numberGroups) {
        List<Group> groups = generator.generate(numberGroups);
        groups.forEach(this::addGroupToBase);
    }

    public List<Group> getGroupsWithLessEqualsStudentCount(int studentCount) {

        try {
            return groupDao.getGroupsWithLessEqualsStudentCount(studentCount);
        } catch (DAOException e) {
            throw new DomainException(MESSAGE_GET_EXCEPTION, e);
        }
    }

    public void addGroupToBase(Group group) {
        try {
            groupDao.add(group);
        } catch (DAOException e) {
            throw new DomainException(
                    String.format(MASK_MESSAGE_ADD_EXCEPTION, group), e);
        }
    }
}
