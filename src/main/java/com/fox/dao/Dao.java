package com.fox.dao;

import java.util.List;
import java.util.Optional;

import com.fox.exception.DAOException;

public interface Dao<T> {

    void add(T entity) throws DAOException;
    
    Optional<T> getById(int id) throws DAOException;
    
    List<T> getAll() throws DAOException;
    
    void update(T entity) throws DAOException;
    
    void delete(T entity) throws DAOException;
    
}
