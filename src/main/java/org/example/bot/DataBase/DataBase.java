package org.example.bot.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private static final String URL = "jdbc:mysql://localhost:3306/myDB";
    private static final String USER = "root";
    private static final String PASSWORD = "rootpassword";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Соединение установлено успешно!");
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных");
            e.printStackTrace();
        }

        return connection;
    }
}
