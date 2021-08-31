package com.alexeykadilnikov.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {
    private static Connection conn;

    private DBUtils() {
    }

    public static Connection getConnection() throws IOException, SQLException {
        if(conn == null) {
            FileInputStream fis = new FileInputStream("properties\\database.yml");
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            String url = (String) properties.get("url");
            String username = (String) properties.get("user");
            String password = (String) properties.get("password");

            conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore", "root", "1111");
        }
        return conn;
    }
}
