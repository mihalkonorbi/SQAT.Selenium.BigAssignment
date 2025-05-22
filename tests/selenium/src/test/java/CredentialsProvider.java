package src.test.java;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CredentialsProvider {
    private static final Properties props = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/java/test-credentials.properties");
            props.load(fis);
        } catch (IOException e) {
            try {
                FileInputStream fis = new FileInputStream("src/test/java/credentials.properties");
                props.load(fis);
            } catch (IOException ex) {
                throw new RuntimeException("Could not load the credentials", ex);
            }
        }
    }

    public static String getUsername() {
        return props.getProperty("username");
    }

    public static String getPassword() {
        return props.getProperty("password");
    }
}