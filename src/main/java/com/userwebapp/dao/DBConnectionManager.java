package com.userwebapp.dao;

import java.sql.*;

public class DBConnectionManager {

    private static final String URL =
            "jdbc:mysql://localhost:3306/userdb?serverTimezone=UTC";

    private static final String USER = "dbuser";
    private static final String PASS = "dbpass";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println(
                    "💡 Connected via JDBC to: "
                            + conn.getMetaData().getURL()
                            + " as user: "
                            + conn.getMetaData().getUserName()
            );
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ DB connection failed: " + e.getMessage());
            throw e;
        }
    }
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✔ Connected to catalog: "
                    + conn.getCatalog()
                    + " via URL: " + conn.getMetaData().getURL()
                    + " as user: " + conn.getMetaData().getUserName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}