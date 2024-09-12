package org.example.bot.dao;

import com.fasterxml.jackson.core.JsonEncoding;
import org.example.bot.Object.Person;
import org.example.bot.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class PersonDAO {



    public Map<?, ?> getBookedForMonday () {
        return bookedForMonday;
    }

    public Map<?, ?> getBookedForWednesday () {
        return bookedForWednesday;
    }

    public Map<?, ?> setBookedForWednesday () {
        return bookedForMonday;
    }

    Map<Long, Person> bookedForMonday;
    Map<Long, Person> bookedForWednesday;


    public void addToAQueue(Connection connection, SendMessage sendMessage, Person person) throws SQLException {

        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String SQL = "INSERT INTO peopleInQueue VALUES(" + person.getChatId() + ", \"" + person.getTelegramUsername() + "\", \"" + person.getNameAndSurname() + "\")";
        System.out.println(SQL);
        statement.executeUpdate(SQL);
        sendMessage.setText("Регистрация прошла успешно!\nВаше место в очереди: " + (getMondaySize() + 1));
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Назад");
        inlineKeyboardButton1.setCallbackData("back");
        rowInline.add(inlineKeyboardButton1);
        markupInline.setKeyboard(Collections.singletonList(rowInline));
        sendMessage.setReplyMarkup(markupInline);


    }

    public void passQueue (SendMessage sendMessage, Connection connection, long chatId, String table) throws SQLException {
        Statement statement = connection.createStatement();
        String SQL = "Delete from " + table + " \nwhere chatId = " + chatId;
        System.out.println(SQL);
        int resultSet = statement.executeUpdate(SQL);
        System.out.println("прошло");
        sendMessage.setText("Вы покинули очередь!");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("Назад");
        inlineKeyboardButton1.setCallbackData("back");

        rowInline.add(inlineKeyboardButton1);

        markupInline.setKeyboard(Collections.singletonList(rowInline));
        sendMessage.setReplyMarkup(markupInline);



    }

    public void initializeLists(Connection connection) throws SQLException {
        bookedForMonday = new HashMap<>();
        bookedForWednesday = new HashMap<>();

        Statement statement = connection.createStatement();
        String SQL = "SELECT * FROM bookedForMonday";
        System.out.println(SQL);
        ResultSet resultSet = statement.executeQuery(SQL);

        while (resultSet.next()) {
            Person person = new Person();

            person.setChatId(resultSet.getLong("chatId"));
            person.setTelegramUsername(resultSet.getString("telegramUsername"));
            person.setNameAndSurname(resultSet.getString("nameAndSurname"));
            bookedForMonday.put(person.getChatId(), person);
        }

        SQL = "SELECT * FROM bookedForWednesday";
        resultSet = statement.executeQuery(SQL);
        while (resultSet.next()) {
            Person person = new Person();
            person.setChatId(resultSet.getLong("chatId"));
            person.setTelegramUsername(resultSet.getString("telegramUsername"));
            person.setNameAndSurname(resultSet.getString("nameAndSurname"));
            bookedForWednesday.put(person.getChatId(), person);

        }

    }

    private long chatId;

    private int serialIdForMonday;



    public void addNewUserForMonday(Person person, Connection connection, SendMessage sendMessage) throws SQLException {

        Statement statement = connection.createStatement();
        String SQL = "INSERT INTO bookedForMonday VALUES(" + person.getChatId() + ", \"" + person.getTelegramUsername() + "\", \"" + person.getNameAndSurname() + "\")";
        System.out.println(SQL);
        statement.executeUpdate(SQL);
        sendMessage.setText("Регистрация прошла успешно!\nВаше место в очереди: " + (getMondaySize() + 1));
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("Назад");
        inlineKeyboardButton1.setCallbackData("back");

        rowInline.add(inlineKeyboardButton1);

        markupInline.setKeyboard(Collections.singletonList(rowInline));
        sendMessage.setReplyMarkup(markupInline);

    }

    public void addNewUserForWednesday(Person person, Connection connection, SendMessage sendMessage) throws SQLException {

        Statement statement = connection.createStatement();
        String SQL = "INSERT INTO bookedForWednesday VALUES(" + person.getChatId() + ", \"" + person.getTelegramUsername() + "\", \"" + person.getNameAndSurname() + "\")";
        System.out.println(SQL);
        statement.executeUpdate(SQL);
        sendMessage.setText("Регистрация прошла успешно!\nВаше место в очереди: " + (getWednesdaySize() + 1));


    }


    public int getMondaySize() {
        return bookedForMonday.size();
    }

    public int getWednesdaySize() {
        return bookedForWednesday.size();
    }

    public void seeTheQueue(SendMessage sendMessage, TelegramBot bot) {
        if (!bookedForMonday.isEmpty()) {
            sendMessage.setText("Записались на понедельник:");
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            for (Person p : bookedForMonday.values()) {
                sendMessage.setText(p.toStringForAdmin());
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (!bookedForWednesday.isEmpty()) {
            sendMessage.setText("Записались на среду:");
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            for (Person p : bookedForWednesday.values()) {
                sendMessage.setText(p.toStringForAdmin());
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }





        sendMessage.setText("Меню: ");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Назад");
        inlineKeyboardButton1.setCallbackData("back_for_admin");

        rowInline.add(inlineKeyboardButton1);

        markupInline.setKeyboard(Collections.singletonList(rowInline));
        sendMessage.setReplyMarkup(markupInline);


    }
}
