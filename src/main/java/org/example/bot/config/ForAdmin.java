package org.example.bot.config;

import org.example.bot.dao.PersonDAO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ForAdmin {
    public void onAdminLogin(SendMessage sendMessage) {
        sendMessage.setText("Добро пожаловать в панель администратора!\nВыберите действие");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline_1 = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Управление очередью");
        inlineKeyboardButton1.setCallbackData("CheckQueue");

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Работа");
        inlineKeyboardButton2.setCallbackData("Work");

        rowInline_1.add(inlineKeyboardButton1);
        rowInline_1.add(inlineKeyboardButton2);

        rowsInline.add(rowInline_1);

        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

    }

    public void CheckQueue (SendMessage sendMessage) {
        sendMessage.setText("Раздел управления очередью");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline_1 = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Просмотреть очередь");
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
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
            logic.setMondayValueIndicator((byte) 6);
        } else {
            logic.setWednesdayValueIndicator((byte) 6);
        }


    }
}
