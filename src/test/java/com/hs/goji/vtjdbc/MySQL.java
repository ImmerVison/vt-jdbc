package com.hs.goji.vtjdbc;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQL extends DB{

    public static final MySQL INSTANCE = new MySQL();
    private static final String TEST_DB;
    static {
        try {
            final Properties pwd = new Properties();
            try(final FileInputStream in = new FileInputStream("/home/goji/pwd.properties")) {
                pwd.load(in);
            }
            TEST_DB = "jdbc:mysql://localhost:3306/vtjdbc?user=vtjdbc&password=" + pwd.getProperty("VTJDBC_DB_PASSWORD");
            System.out.println("URL: " + TEST_DB);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(TEST_DB);
    }
}
