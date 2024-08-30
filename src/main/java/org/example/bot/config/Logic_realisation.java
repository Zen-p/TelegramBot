package org.example.bot.config;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Logic_realisation implements Logic{
    @Override
    public List<?> getRowInline() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Записаться");
        inlineKeyboardButton1.setCallbackData("Записаться");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Просмотреть очередь");
        inlineKeyboardButton2.setCallbackData("Просмотреть очередь");
        rowInline.add(inlineKeyboardButton1);
        rowInline.add(inlineKeyboardButton2);
        return rowInline;
    }
}
