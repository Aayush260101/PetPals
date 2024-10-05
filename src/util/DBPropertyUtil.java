package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {

    // Load properties using FileInputStream
    public static Properties loadProperties(String propertiesFileName) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(propertiesFileName)) {
            properties.load(input);
            // Debugging statement to confirm loading without printing sensitive info
            System.out.println("Properties loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading properties file: " + e.getMessage());
        }
        return properties;
    }
}
