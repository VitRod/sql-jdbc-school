package com.fox.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.fox.exception.DAOException;
import com.fox.reader.Reader;

public class ConnectionProvider {
	
	private static final String FILENAME_DB_PROPERTIES = "db.properties";
	private static final String DB_DRIVER = "db.driver";
	private static final String DB_PASSWORD = "db.password";
	private static final String DB_LOGIN = "db.login";
	private static final String DB_URL = "db.url";
	private static final String MESSAGE_DRIVER_NOT_FOUND = "Driver not found";
	private static final String MESSAGE_CONNECTION_ERROR = "Connection failed";

	private static String url;
	private static String login;
	private static String password;

	public ConnectionProvider() {
	}
	
	static {
		Reader reader = new Reader();
		Properties properties = reader.readProperties(FILENAME_DB_PROPERTIES);
		try {
			Class.forName(properties.getProperty(DB_DRIVER));
		} catch (ClassNotFoundException e) {
			System.out.println(MESSAGE_DRIVER_NOT_FOUND);
			System.out.println(MESSAGE_CONNECTION_ERROR);
			e.printStackTrace();
		}

		url = properties.getProperty(DB_URL);
		login = properties.getProperty(DB_LOGIN);
		password = properties.getProperty(DB_PASSWORD);

	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, login, password);
	}
}
