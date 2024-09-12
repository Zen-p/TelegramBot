package org.example.bot.config;

import org.example.bot.dao.PersonDAO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ForAdmin {
    public void onAdminLogin(SendMessage sendMessage) {
        sendMessage.setText("Добро пожаловать в панель администратора!");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline_1 = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Посмотреть очередь");
        inlineKeyboardButton1.setCallbackData("see_the_queue");

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Добавить в очередь");
        inlineKeyboardButton2.setCallbackData("addToQueue");

        rowInline_1.add(inlineKeyboardButton1);
        rowInline_1.add(inlineKeyboardButton2);

        rowsInline.add(rowInline_1);

        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

    }



    public void addToQueue (Logic_realisation logic) {
        logic.setValueIndicator((byte) 6);




    }
}
