package org.example.bot;

import org.example.bot.config.Logic_realisation;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot{



    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (text.equals("/start")) {
            sendMessage.setText("Добро пожаловать в нашу парикмахерскую!\n" +
                    "Мы рады видеть вас и готовы помочь вам записаться на услуги парикмахера");
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            Logic_realisation logic = new Logic_realisation();
            markupInline.setKeyboard(Collections.singletonList((List<InlineKeyboardButton>) logic.getRowInline()));
            sendMessage.setReplyMarkup(markupInline);




        }
        else {
            sendMessage.setText(null);
        }




        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "BarberAssist_bot";
    }
    @Override
    public String getBotToken () {
        return "7214428459:AAF_rPscG7Q6x3eZa8j5Hj4g-a7KKq4fLSM";
    }


}
