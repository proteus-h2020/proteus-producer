package eu.proteus.producer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveDB {
	
	/**
	 * Hive JDBC driver
	 */
	public static final String JDBC_DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
	
	/**
	 * Execute an SQL query 
	 * @param url The connection to execute the query in
	 * @param user The name of the user the queries should run as
	 * @param password the user's password
	 * @param query The query to execute
	 * @param rsh The handler that converts the results into an object
	 * @return The object returned by the handler
	 * @throws SQLException if a database access error occurs
	 */
	public static <T> T query(String url, String user, String password, String query, ResultSetHandler<T> rsh) throws SQLException {
		Connection connection = null;
		try {
			// Connect to Hive 
			// Example: jdbc:hive2://hiveserver:10000/;ssl=false
			Class.forName(JDBC_DRIVER_NAME);			
			connection = DriverManager.getConnection(url, user, password);
			
			// Init Statement
			Statement state = connection.createStatement();
			
			// Execute SELECT Query
			ResultSet rs = state.executeQuery(query);
			return rsh.handle(rs);
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Hive driver not found");
		} finally {
			if (connection != null) {
				connection.close();
			}
		}	 
	}
	 
}
