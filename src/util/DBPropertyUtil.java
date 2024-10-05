package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBPropertyUtil {
    /**
     * Retrieves a database connection from the specified properties file.
     *
     * @param propertiesFileName The path to the properties file containing DB connection details.
     * @return A Connection object, or null if unable to establish a connection.
     */
    public static Connection getConnectionFromProperties(String propertiesFileName) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(propertiesFileName)) {
            properties.load(input);
            String url = properties.getProperty("db.connectionString"); // Adjusted property name
            String user = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            return DriverManager.getConnection(url, user, password);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null; // Return null if there's an error
        }
    }
}
