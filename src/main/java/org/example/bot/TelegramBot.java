package org.example.bot;

import org.example.bot.DataBase.DataBase;
import org.example.bot.Object.Person;
import org.example.bot.config.ForAdmin;
import org.example.bot.config.Logic_realisation;
import org.example.bot.dao.PersonDAO;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelegramBot extends TelegramLongPollingBot {


    Connection connection = DataBase.getConnection();
    private final HashMap<Long, Message> sentMessages = new HashMap<>();

    @Override
    public String getBotUsername() {
        return "BarberAssist_bot";

    }

    @Override
    public String getBotToken() {
        return "7214428459:AAF_rPscG7Q6x3eZa8j5Hj4g-a7KKq4fLSM";

    }

    private final HashMap<Long, String> userKeys = new HashMap<>();

    private String getKey(Long chatId) {
        return userKeys.get(chatId);
    }

    private void setKey(Long chatId, String key) {
        userKeys.put(chatId, key);
    }

    private void clearKey(Long chatId) {
        userKeys.remove(chatId);
    }

    PersonDAO dao = new PersonDAO();
    long chatId;
    Logic_realisation logic = new Logic_realisation();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);  // 10 потоков

    @Override
    public void onUpdateReceived(Update update) {

        executorService.submit(() -> {
            handleMessage(update);
        });
    }

    private void handleMessage(Update update) {
        ForAdmin forAdmin = new ForAdmin();


        SendMessage sendMessage = new SendMessage();

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            sendMessage.setChatId(chatId);
            if (update.getMessage().getText().equals("купите леново")) {
                deletePreviousMessage(chatId);
                forAdmin.onAdminLogin(sendMessage);
            }
            else if (update.getMessage().getText().equals("update")) {
                Connection connection = DataBase.getConnection();

            }
            else if (update.getMessage().getText().equals("/start")) {
                System.out.println(update.getMessage().getChatId());
                logic.showHelloMessage(sendMessage);
                deletePreviousMessage(chatId);

            }
            else if ("bookForMonday".equals(getKey(chatId))) {
                clearKey(chatId);
                deletePreviousMessage(chatId);

                Person person = new Person(update.getMessage().getFrom().getUserName(), chatId, update.getMessage().getText());
                try {
                    dao.addNewUserForMonday(person, connection, sendMessage);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if ("bookForWednesday".equals(getKey(chatId))) {
                clearKey(chatId);
                deletePreviousMessage(chatId);

                Person person = new Person(update.getMessage().getFrom().getUserName(), chatId, update.getMessage().getText());
                try {
                    dao.addNewUserForWednesday(person, connection, sendMessage);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                inlineKeyboardButton1.setText("Назад");
                inlineKeyboardButton1.setCallbackData("back");
                rowInline.add(inlineKeyboardButton1);
                markupInline.setKeyboard(Collections.singletonList(rowInline));
                sendMessage.setReplyMarkup(markupInline);
            } else if ("put_person_in_queue".equals(getKey(chatId))) {
                clearKey(chatId);
                try {
                    dao.initializeQueueList(connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                deletePreviousMessage(chatId);

                if (dao.getPeopleInQueue().containsKey(chatId)) {
                    sendMessage.setText("Вы уже стоите в очереди");
                } else {
                    Person person = new Person(update.getMessage().getFrom().getUserName(), chatId, update.getMessage().getText());
                    try {
                        dao.addToAQueue(connection, sendMessage, person);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                inlineKeyboardButton1.setText("Назад");
                inlineKeyboardButton1.setCallbackData("back");
                rowInline.add(inlineKeyboardButton1);
                markupInline.setKeyboard(Collections.singletonList(rowInline));
                sendMessage.setReplyMarkup(markupInline);
            } else if (update.getMessage().getText().equals("отец") || update.getMessage().getText().equals("Отец")) {
                deletePreviousMessage(chatId);

                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatId);
                sendPhoto.setPhoto(new InputFile("https://clck.ru/3DMqAE"));

                sendPhoto.setCaption("Все наши клиенты для нас как родные дети");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                inlineKeyboardButton1.setText("Назад");
                inlineKeyboardButton1.setCallbackData("back");

                rowInline.add(inlineKeyboardButton1);

                markupInline.setKeyboard(Collections.singletonList(rowInline));
                sendPhoto.setReplyMarkup(markupInline);
                try {
                    sentMessages.put(chatId, execute(sendPhoto));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            sendMessage.setChatId(chatId);

            if (update.getCallbackQuery().getData().equals("back")) {
                deletePreviousMessage(chatId);
                logic.showHelloMessage(sendMessage);
            } else if (update.getCallbackQuery().getData().equals("signup")) {
                deletePreviousMessage(chatId);
                try {
                    dao.initializeLists(connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    logic.onSignUp(sendMessage, dao, chatId);
                } catch (Exception e) {
                    throw e;
                }

            } else if (update.getCallbackQuery().getData().equals("workTime")) {
                deletePreviousMessage(chatId);
                logic.showWorkTime(sendMessage);
            } else if (update.getCallbackQuery().getData().equals("passMonday")) {
                deletePreviousMessage(chatId);
                try {
                    dao.passQueue(sendMessage, connection, chatId, "bookedForMonday");
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } else if (update.getCallbackQuery().getData().equals("passWednesday")) {
                deletePreviousMessage(chatId);
                try {
                    dao.passQueue(sendMessage, connection, chatId, "bookedForWednesday");
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } else if ("bookForMonday".equals(update.getCallbackQuery().getData())) {
                deletePreviousMessage(chatId);
                sendMessage.setText("Пожалуйста, укажите ваше имя и фамилию для подтверждения записи");
                setKey(chatId, "bookForMonday");
            } else if (update.getCallbackQuery().getData().equals("back_for_admin")) {
                deletePreviousMessage(chatId);
                forAdmin.onAdminLogin(sendMessage);
            } else if ("bookForWednesday".equals(update.getCallbackQuery().getData())) {
                deletePreviousMessage(chatId);
                sendMessage.setText("Пожалуйста, укажите ваше имя и фамилию для подтверждения записи");
                setKey(chatId, "bookForWednesday");
            } else if (update.getCallbackQuery().getData().equals("next")) {
                deletePreviousMessage(chatId);
                sendMessage.setText("Следующий по очереди: ");
                try {
                    dao.next(sendMessage, new Person(null, 0, null), connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (update.getCallbackQuery().getData().equals("done")) {
                deletePreviousMessage(chatId);
                sendMessage.setText("Следующий по очереди: ");
                try {
                    dao.done(sendMessage, new Person(null, 0, null), connection, chatId, this);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (update.getCallbackQuery().getData().equals("see_the_queue")) {
                deletePreviousMessage(chatId);
                try {
                    dao.initializeLists(connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    dao.seeTheQueue(sendMessage, this);
                } catch (Exception e) {
                    throw e;
                }


            } else if (update.getCallbackQuery().getData().equals("addToQueue")) {
                deletePreviousMessage(chatId);
                forAdmin.addToQueue(logic, sendMessage);

            } else if (update.getCallbackQuery().getData().equals("CheckQueue")) {
                deletePreviousMessage(chatId);
                forAdmin.CheckQueue(sendMessage);

            } else if (update.getCallbackQuery().getData().equals("Work")) {

                deletePreviousMessage(chatId);
                forAdmin.showWorkMenu(sendMessage);

            } else if ("put_person_in_queue".equals(update.getCallbackQuery().getData())) {
                deletePreviousMessage(chatId);
                try {
                    dao.initializeQueueList(connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (dao.getPeopleInQueue().containsKey(chatId)) {
                    sendMessage.setText("Вы уже стоите в очереди");
                } else {
                    sendMessage.setText("Пожалуйста, укажите ваше имя и фамилию для подтверждения записи в очередь");
                    setKey(chatId, "put_person_in_queue");
                }
            } else if (update.getCallbackQuery().getData().equals("portfolio")) {
                deletePreviousMessage(chatId);

                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatId);
                sendPhoto.setPhoto(new InputFile("https://clck.ru/3Cx75s"));

                sendPhoto.setCaption("У нас стригутся самые знаменитые личности");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                inlineKeyboardButton1.setText("Назад");
                inlineKeyboardButton1.setCallbackData("back");

                rowInline.add(inlineKeyboardButton1);

                markupInline.setKeyboard(Collections.singletonList(rowInline));
                sendPhoto.setReplyMarkup(markupInline);
                try {
                    sentMessages.put(chatId, execute(sendPhoto));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            sentMessages.put(chatId, execute(sendMessage));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePreviousMessage(long chatId) {
        Message sent = sentMessages.get(chatId);
        if (sent == null) {
            return;
        }
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(sent.getMessageId());
        deleteMessage.setChatId(chatId);
        try {
            execute(deleteMessage);
            sentMessages.put(chatId, null);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}

