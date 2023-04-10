package com.ruslan.checmitings.db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:courtdata.db";
            return DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Problem with connection of database");
            return null;
        }
    }
}
