package org.example.bot.dao;

import org.example.bot.Object.Person;
import org.example.bot.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class PersonDAO {

    public void initializeMondayList (Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String SQL = "SELECT * FROM bookedForMonday";
        ResultSet resultSet = statement.executeQuery(SQL);
        System.out.println("вывод:");
        while(resultSet.next()) {
            Person person = new Person();

            person.setChatId(resultSet.getLong("chatId"));
            person.setTelegramUsername(resultSet.getString("telegramUsername"));
            person.setNameAndSurname(resultSet.getString("nameAndSurname"));


            bookedForMonday.add(person);
                System.out.println(person.toString());



        }


    }

    private long chatId;

    private int serialIdForMonday;

    public int getSerialIdForMonday() {
        return serialIdForMonday;
    }

    public void setSerialIdForMonday(int serialIdForMonday) {
        this.serialIdForMonday = serialIdForMonday;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    List<Person> bookedForMonday;
    List<Person> bookedForWednesday;

    {
        bookedForMonday = new LinkedList<>();
        bookedForWednesday = new LinkedList<>();
    }


    public void addNewUserForMonday(Person person, Connection connection) throws SQLException {

        Statement statement = connection.createStatement();
        String SQL = "INSERT INTO bookedForMonday VALUES(" + person.getChatId() + ", \"" + person.getTelegramUsername() + "\", \"" + person.getNameAndSurname() + "\")";
        System.out.println(SQL);
        statement.executeUpdate(SQL);

    }


    public int getMondaySize() {
        return bookedForMonday.size();
    }

    public int getWednesdaySize() {
        return bookedForWednesday.size();
    }

    public void seeTheQueue(SendMessage sendMessage, TelegramBot bot, Connection connection) {
        if (bookedForMonday.isEmpty()) {
            try {
                this.initializeMondayList(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        sendMessage.setText("Записались на понедельник:");
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        for (Person p : bookedForMonday) {
            sendMessage.setText(p.toStringForAdmin());
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }
    }


}
