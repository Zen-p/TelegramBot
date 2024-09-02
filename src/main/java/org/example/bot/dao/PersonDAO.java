package org.example.bot.dao;

import com.sun.security.jgss.GSSUtil;
import org.example.bot.Object.Person;

import org.example.bot.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


public class PersonDAO {

    int barberId;

    List<Person> bookedForMonday;
    List<Person> bookedForWednesday;

    {
        bookedForMonday = new LinkedList<>();
        bookedForWednesday = new LinkedList<>();

    }

    public void register(long chatId, SendMessage sendMessage, Update update) {
        Calendar calendar = new GregorianCalendar();
        if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {
            sendMessage.setText("Пожалуйста, укажите ваше имя и фамилию для подтверждения записи");
            TelegramBot bot = new TelegramBot();
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }






        }
    }
}
