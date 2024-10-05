package util;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseContext {
    private Connection connection;

    /**
     * Constructor that initializes the database connection.
     */
    public DatabaseContext() {
        this.connection = null; // Initialize connection to null
    }

    /**
     * Gets a database connection.
     *
     * @return A Connection object.
     * @throws SQLException If unable to obtain a connection.
     */
    public Connection getConnection() throws SQLException {
        // Get a new connection using DBConnUtil
        if (connection == null || connection.isClosed()) {
            connection = DBConnUtil.getConnection(
                "jdbc:mysql://localhost:3306/petpal1", // Adjust as necessary
                "root", // Adjust as necessary
                "Abcd1234@" // Adjust as necessary
            );
        }
        return connection;
    }

    /**
     * Closes the current database connection.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Set to null after closing
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
