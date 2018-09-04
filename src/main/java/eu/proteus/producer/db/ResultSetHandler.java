package eu.proteus.producer.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler<T> {

	/**
	 * Turn the ResultSet into an object
	 * @param rs the ResultSet to handle
	 * @return An object initialized with ResultSet data
	 * @throws SQLException if a database access error occurs
	 */
	public T handle(ResultSet rs) throws SQLException;
	
}
