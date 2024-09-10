package org.example.bot.dao;

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

    public Map<?, ?> bookedForWednesday () {
        return bookedForMonday;
    }

    Map<Long, Person> bookedForMonday;
    Map<Long, Person> bookedForWednesday;

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

    public void seeTheQueue(SendMessage sendMessage, TelegramBot bot, Connection connection) {
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
