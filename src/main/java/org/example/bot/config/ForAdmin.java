package org.example.bot.config;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ForAdmin {
    public void onAdminLogin (Update update, SendMessage sendMessage) {
        sendMessage.setText("Добро пожаловать в панель администратора!");
    }
}
