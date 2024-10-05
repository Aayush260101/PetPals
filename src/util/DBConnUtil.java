package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnUtil {
    private Connection connection;

    // Constructor
    public DBConnUtil() {
        this.connection = null;
    }

    // Get connection using properties from db.properties
    public Connection getConnectionFromProperties(String propertiesFileName) throws SQLException {
        Properties properties = DBPropertyUtil.loadProperties(propertiesFileName);
        if (properties != null) {
            String url = properties.getProperty("db.connectionString");
            String user = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            return getConnection(url, user, password); // Delegate to getConnection method
        }
        return null;
    }

    // Get connection using provided credentials
    public Connection getConnection(String url, String user, String password) throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    // Close the connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Reset connection after closing
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
