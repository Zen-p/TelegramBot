package org.example.bot.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private static final String URL = "jdbc:mysql://sql.freedb.tech:3306/freedb_UsersOfBot"; //sql.freedb.tech freedb_UsersOfBot
    private static final String USER = "freedb_RootUser";//freedb_RootUser
    private static final String PASSWORD = "P7Jgd24jFa7@nZf";//P7Jgd24jFa7@nZf";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("успешно");
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных");
            e.printStackTrace();
        }

        return connection;
    }
}
