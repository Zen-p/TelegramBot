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
    int count;

    Map<Long, Person> bookedForMonday;
    Map<Long, Person> bookedForWednesday;
    Map<Long, Person> peopleInQueue;
    private Person lastPerson = new Person(null, 0, null);

    public Map<?, ?> getBookedForMonday() {
        return bookedForMonday;
    }
    public Map<?, ?> getBookedForWednesday() {
        return bookedForWednesday;
    }
    public Map<Long, Person> getPeopleInQueue() {
        return peopleInQueue;
    }
    public int getMondaySize() {
        return bookedForMonday.size();
    }
    public int getWednesdaySize() {
        return bookedForWednesday.size();
    }

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
        sendMessage.setText("Вы стали в очередь!\n При появлении свободных мест вы будете уведомлены");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Назад");
        inlineKeyboardButton1.setCallbackData("back");
        rowInline.add(inlineKeyboardButton1);
        markupInline.setKeyboard(Collections.singletonList(rowInline));
        sendMessage.setReplyMarkup(markupInline);


    }

    public void initializeQueueList(Connection connection) throws SQLException {
        peopleInQueue = new LinkedHashMap<>();

        Statement statement = connection.createStatement();
        String SQL = "SELECT * FROM peopleInQueue";
        System.out.println(SQL);
        ResultSet resultSet = statement.executeQuery(SQL);

        while (resultSet.next()) {
            Person person = new Person();

            person.setChatId(resultSet.getLong("chatId"));
            person.setTelegramUsername(resultSet.getString("telegramUsername"));
            person.setNameAndSurname(resultSet.getString("nameAndSurname"));
            peopleInQueue.put(person.getChatId(), person);

        }

    }
    public void initializeLists(Connection connection) throws SQLException {
        bookedForMonday = new LinkedHashMap<>();
        bookedForWednesday = new LinkedHashMap<>();

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

    public void next(SendMessage sendMessage, Person person, Connection connection, TelegramBot bot, Long chatId) throws SQLException {

        Calendar calendar = Calendar.getInstance();
        Statement statement = connection.createStatement();
        String dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ? "bookedForMonday" : "bookedForWednesday");
        String SQL = "select * from " + dayOfWeek + " LIMIT 1";
        System.out.println(SQL);
        ResultSet resultSet = statement.executeQuery(SQL);
        System.out.println("Успешно");
        while (resultSet.next()) {
            person.setChatId(resultSet.getLong("chatId"));
            person.setTelegramUsername(resultSet.getString("telegramUsername"));
            person.setNameAndSurname(resultSet.getString("nameAndSurname"));
        }

        if (person.getNameAndSurname() == null && person.getTelegramUsername() == null) {
            sendMessage.setText("Очередь пуста");
            System.out.println(person.getChatId());
        } else {


            sendMessage.setText("Брат, ща тебя постригут — по красоте будет! \n" +
                    "Парикмахер уже руку набил, ножницы как автомат стреляют. Ты только не дергайся, а то сейчас тебя, как барана на живую, подстригут, и будешь ровный как футбольное поле, понял? \n" +
                    "В кресло садись, не тупи, выходишь потом как босс, все оглядываться будут!");
            sendMessage.setChatId(person.getChatId());
            try {
                bot.execute(sendMessage);
                System.out.println("сообщение о начале работы отправлено");
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            sendMessage.setChatId(chatId);


            sendMessage.setText("Следующий по очереди:\n\n" + person.toStringForAdmin());

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline_1 = new ArrayList<>();

            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText("назад");
            inlineKeyboardButton1.setCallbackData("back_for_admin");
            rowInline_1.add(inlineKeyboardButton1);

            List<InlineKeyboardButton> rowInline_2 = new ArrayList<>();
            InlineKeyboardButton done = new InlineKeyboardButton();
            done.setText("Пострижен(а)! 💇🏿‍");
            done.setCallbackData("done");


            rowInline_2.add(done);

            rowsline.add(rowInline_2);
            rowsline.add(rowInline_1);

            markupInline.setKeyboard(rowsline);
            sendMessage.setReplyMarkup(markupInline);
            lastPerson = person;
        }


    }
    public void notifyUsers(Calendar calendar, SendMessage sendMessage, TelegramBot bot) throws TelegramApiException {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {


            for (Map.Entry<Long, Person> entry : bookedForMonday.entrySet()) {

                if (count == 0) {
                    bot.deletePreviousMessage(entry.getKey());
                    sendMessage.setChatId(entry.getKey());
                    sendMessage.setText("Спасибо за визит! \uD83D\uDE0A Надеемся, что ваша новая стрижка " +
                            "вам понравилась. \uD83D\uDC87\u200D♂\uFE0F Если у вас есть какие-либо вопросы или пожелания, не" +
                            " стесняйтесь обращаться к нам. \uD83D\uDCDE" +
                            " Не забудьте записаться на следующую стрижку. " +
                            "Ждем вас снова! \uD83D\uDC87\u200D♀\uFE0F");
                    count++;
                    try {
                        bot.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                } else if (count == 1) {
                    bot.deletePreviousMessage(entry.getKey());
                    sendMessage.setChatId(entry.getKey());
                    sendMessage.setText("Брат, ща тебя постригут — по красоте будет! \n" +
                            "Парикмахер уже руку набил, ножницы как автомат стреляют. Ты только не дергайся, а то сейчас тебя, как барана " +
                            "на живую, подстригут, и будешь ровный как футбольное поле, понял? \n" +
                            "В кресло садись, не тупи, выходишь потом как босс, все оглядываться будут!");
                    count++;
                    try {
                        bot.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    bot.deletePreviousMessage(entry.getKey());
                    sendMessage.setChatId(entry.getKey());
                    sendMessage.setText("Ваша очередь стала ближе!\n\nваша позиция в очереди: " + count);
                    try {
                        bot.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    count++;
                }
            }
            count = 0;
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {

            for (Map.Entry<Long, Person> entry : bookedForWednesday.entrySet()) {

                if (count == 0) {
                    sendMessage.setChatId(entry.getKey());
                    sendMessage.setText("Спасибо за визит! \uD83D\uDE0A Надеемся, что ваша новая стрижка " +
                            "вам понравилась. \uD83D\uDC87\u200D♂\uFE0F Если у вас есть какие-либо вопросы или пожелания, не" +
                            " стесняйтесь обращаться к нам. \uD83D\uDCDE" +
                            " Не забудьте записаться на следующую стрижку. " +
                            "Ждем вас снова! \uD83D\uDC87\u200D♀\uFE0F");
                    count++;
                    try {
                        bot.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    sendMessage.setChatId(entry.getKey());
                    sendMessage.setText("Ваша очередь стала ближе!\n\nваша позиция в очереди: " + count);
                    try {
                        bot.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    count++;
                }
            }
            count = 0;
        }


    }
    public void done(SendMessage sendMessage, Person person, Connection connection, Long chatId, TelegramBot bot) throws SQLException, TelegramApiException {
        bot.deletePreviousMessage(chatId);
        Calendar calendar = Calendar.getInstance();
        Statement statement = connection.createStatement();


        String dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ? "bookedForMonday" : "bookedForWednesday");
        String SQL = "delete from " + dayOfWeek + "\n" + "where chatId = " + lastPerson.getChatId();
        initializeLists(connection);
        notifyUsers(calendar, sendMessage, bot);
        sendMessage.setChatId(chatId);
        int res = statement.executeUpdate(SQL);
        System.out.println(SQL);

        SQL = "select * from " + dayOfWeek + " LIMIT 1";
        System.out.println(SQL);
        ResultSet resultSet = statement.executeQuery(SQL);
        System.out.println("Успешно");
        while (resultSet.next()) {
            person.setChatId(resultSet.getLong("chatId"));
            person.setTelegramUsername(resultSet.getString("telegramUsername"));
            person.setNameAndSurname(resultSet.getString("nameAndSurname"));
        }

        if (person.getNameAndSurname() == null && person.getTelegramUsername() == null) {
            sendMessage.setText("Очередь пуста");
            System.out.println(person.getChatId());
        } else {

            sendMessage.setText("Следующий по очереди:\n\n" + person.toStringForAdmin());

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline_1 = new ArrayList<>();

            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText("назад");
            inlineKeyboardButton1.setCallbackData("back_for_admin");
            rowInline_1.add(inlineKeyboardButton1);

            List<InlineKeyboardButton> rowInline_2 = new ArrayList<>();
            InlineKeyboardButton done = new InlineKeyboardButton();
            done.setText("Пострижен(а)! 💇🏿‍");
            done.setCallbackData("done");

            rowInline_2.add(done);

            rowsline.add(rowInline_2);
            rowsline.add(rowInline_1);

            markupInline.setKeyboard(rowsline);
            sendMessage.setReplyMarkup(markupInline);
            lastPerson = person;
        }

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

    public void seeTheQueue(SendMessage sendMessage, TelegramBot bot) {
        if (!bookedForMonday.isEmpty()) {
            sendMessage.setText("Записались на понедельник:");
            try {
                bot.getListOfMessages().add(bot.execute(sendMessage));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            for (Person p : bookedForMonday.values()) {
                sendMessage.setText(p.toStringForAdmin());
                try {
                    bot.getListOfMessages().add(bot.execute(sendMessage));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (!bookedForWednesday.isEmpty()) {
            sendMessage.setText("Записались на среду:");
            try {
                bot.getListOfMessages().add(bot.execute(sendMessage));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            for (Person p : bookedForWednesday.values()) {
                sendMessage.setText(p.toStringForAdmin());
                try {
                    bot.getListOfMessages().add(bot.execute(sendMessage));
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
    public void passQueue(SendMessage sendMessage, Connection connection, long chatId, String table) throws SQLException {
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

}
