package com.fox.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.fox.exception.DAOException;
import com.fox.reader.Reader;

public class StartUpDao {
	
	private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String DELIMITER_QUERIES = ";";
    
    private static final String PROPERTY_SQL_DELETE_TABLES = 
    		"DROP TABLE IF EXISTS groups CASCADE; " + LINE_SEPARATOR +
    		"DROP TABLE IF EXISTS students CASCADE; " + LINE_SEPARATOR +
    		"DROP TABLE IF EXISTS courses CASCADE; " + LINE_SEPARATOR +
    		"DROP TABLE IF EXISTS students_courses CASCADE;";
    
    private static final String FILENAME_SCRIPT_CREATE_TABLES = "schema.sql";
    private static final String MESSAGE_TABLES_READY = "Tables are ready";

    public void prepareTables() throws DAOException {
        deleteTables();
        createTables(FILENAME_SCRIPT_CREATE_TABLES);
        System.out.println(MESSAGE_TABLES_READY);
    }

    private void deleteTables() throws DAOException {
        String sqlCommands = PROPERTY_SQL_DELETE_TABLES;
        
        String[] sql = sqlCommands.split(DELIMITER_QUERIES);

        try (Connection connection = ConnectionProvider.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                connection.setAutoCommit(false);
                for (int i = 0; i < sql.length; i++) {
                    statement.addBatch(sql[i]);
                }
                statement.executeBatch();
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DAOException();
        }
    }

    private void createTables(String scriptFilename) throws DAOException {

        try (Connection connection = ConnectionProvider.getConnection()) {

            SqlScriptRunner scriptRunner = new SqlScriptRunner(connection);
            scriptRunner.runSqlScript(scriptFilename);

        } catch (SQLException e) {
            throw new DAOException("Can't run script", e);
        }
    }
}
