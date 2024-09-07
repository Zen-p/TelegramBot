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

    static String key;

    private String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    long chatId;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);  // 10 потоков

    @Override
    public void onUpdateReceived(Update update) {
        // Обработка сообщений в отдельном потоке
        executorService.submit(() -> {
            handleMessage(update);
        });
    }

    private void handleMessage(Update update) {
        PersonDAO dao = new PersonDAO();
        Logic_realisation logic = new Logic_realisation();
        SendMessage sendMessage = new SendMessage();

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            sendMessage.setChatId(chatId);
            if (update.getMessage().getText().equals("hB7$5mG8@z") || update.getMessage().getText().equals("админ")) {
                deletePreviousMessage(chatId);
                ForAdmin forAdmin = new ForAdmin();
                forAdmin.onAdminLogin(sendMessage);
            } else if (update.getMessage().getText().equals("/start")) {
                logic.showHelloMessage(sendMessage);
                deletePreviousMessage(chatId);
            } else if (this.getKey().equals("R9&zK2@Lp1")) {
                this.setKey(null);
                deletePreviousMessage(chatId);

                Person person = new Person(update.getMessage().getFrom().getUserName(), chatId, update.getMessage().getText());
                try {
                    dao.addNewUserForMonday(person, connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                sendMessage.setText("Регистрация прошла успешно!");
            } else if (this.getKey().equals("R9&zK2_2@#p1")) {
                this.setKey(null);
                deletePreviousMessage(chatId);

                Person person = new Person(update.getMessage().getFrom().getUserName(), chatId, update.getMessage().getText());
                try {
                    dao.addNewUserForWednesday(person, connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                sendMessage.setText("Регистрация прошла успешно!");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                inlineKeyboardButton1.setText("Назад");
                inlineKeyboardButton1.setCallbackData("back");

                rowInline.add(inlineKeyboardButton1);

                markupInline.setKeyboard(Collections.singletonList(rowInline));
                sendMessage.setReplyMarkup(markupInline);
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
                logic.onSignUp(chatId, sendMessage, dao, this);
            } else if (update.getCallbackQuery().getData().equals("workTime")) {
                deletePreviousMessage(chatId);
                logic.showWorkTime(sendMessage);
            } else if (update.getCallbackQuery().getData().equals("bookFormMonday")) {
                deletePreviousMessage(chatId);
                sendMessage.setText("Пожалуйста, укажите ваше имя и фамилию для подтверждения записи");
                setKey("R9&zK2@Lp1");
            } else if (update.getCallbackQuery().getData().equals("bookForWednesday")) {
                deletePreviousMessage(chatId);
                sendMessage.setText("Пожалуйста, укажите ваше имя и фамилию для подтверждения записи");
                TelegramBot bot = new TelegramBot();
                setKey("R9&zK2_2@#p1");
            } else if (update.getCallbackQuery().getData().equals("see_the_queue")) {
                deletePreviousMessage(chatId);
                dao.seeTheQueue(sendMessage, this, connection);
            } else if (update.getCallbackQuery().getData().equals("portfolio")) {
                deletePreviousMessage(chatId);

                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatId);
                sendPhoto.setPhoto(new InputFile("https://clck.ru/3Cx75s"));

                sendPhoto.setCaption("Я твоего парикмахера руки ебал");

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
