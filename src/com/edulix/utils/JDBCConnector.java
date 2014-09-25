package com.edulix.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.edulix.constants.DB;

public class JDBCConnector {

	/**
	 * This static method is specifically used to create a connection to the Oracle DB. 
	 * @return Returns a connection to the database.
	 */
	public static Connection createConnection() {
		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
			//System.out.println("Connection successfully established: " + connection);
		} catch (SQLException se) {
			System.err.println("SQL exception caught!");
			se.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception caught!");
			e.printStackTrace();
		}
		
		return connection;
	}

}
