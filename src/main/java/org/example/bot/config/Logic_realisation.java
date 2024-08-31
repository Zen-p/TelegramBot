package org.example.bot.config;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logic_realisation{

    public void showHelloMessage (SendMessage sendMessage) {
        sendMessage.setText("Добро пожаловать в нашу парикмахерскую!\n" +
                "Мы рады видеть вас и готовы помочь вам записаться на услуги парикмахера");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline_1 = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Записаться");
        inlineKeyboardButton1.setCallbackData("signup");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Просмотреть очередь");
        inlineKeyboardButton2.setCallbackData("see_the_queue");

        rowInline_1.add(inlineKeyboardButton1);
        rowInline_1.add(inlineKeyboardButton2);

        List<InlineKeyboardButton> rowInline_2 = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText("портфолио");
        inlineKeyboardButton3.setCallbackData("portfolio");
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        inlineKeyboardButton4.setText("График работы");
        inlineKeyboardButton4.setCallbackData("workTime");

        rowInline_2.add(inlineKeyboardButton3);
        rowInline_2.add(inlineKeyboardButton4);
        rowsInline.add(rowInline_1);
        rowsInline.add(rowInline_2);



        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

    }



}
