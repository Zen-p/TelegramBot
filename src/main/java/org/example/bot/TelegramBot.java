package org.example.bot;

import org.example.bot.Object.Person;
import org.example.bot.config.ForAdmin;
import org.example.bot.config.Logic_realisation;
import org.example.bot.dao.PersonDAO;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "BarberAssist_bot";
    }

    @Override
    public String getBotToken() {
        return "7214428459:AAF_rPscG7Q6x3eZa8j5Hj4g-a7KKq4fLSM";
    }

    @Override
    public void onUpdateReceived(Update update) {
        PersonDAO dao = new PersonDAO();
        Logic_realisation logic = new Logic_realisation();
        SendMessage sendMessage = new SendMessage();
        long chatId = 0;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            sendMessage.setChatId(chatId);

            if (update.getMessage().getText().equals("/start")) {
                logic.showHelloMessage(sendMessage);
            } else if (update.getMessage().getText().equals("Дядя Толик")) {
                ForAdmin forAdmin = new ForAdmin();
                forAdmin.onAdminLogin(update, sendMessage);

            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            sendMessage.setChatId(chatId);

            if (update.getCallbackQuery().getData().equals("signup")) {
                logic.onSignUp(sendMessage);
            } else if (update.getCallbackQuery().getData().equals("workTime")) {
                logic.showWorkTime(sendMessage);
            } else if (update.getCallbackQuery().getData().equals("queueForFirstDay")) {
                logic.showRegisterMenu(sendMessage);


            } else if (update.getCallbackQuery().getData().equals("portfolio")) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatId);
                sendPhoto.setPhoto(new InputFile("https://clck.ru/3Cx75s"));
                sendPhoto.setCaption("Это никита");
                try {
                    execute(sendPhoto);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }


    }


}
