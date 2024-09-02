package org.example.bot.config;

import org.example.bot.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Logic_realisation {

    public void showHelloMessage(SendMessage sendMessage) {
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
        inlineKeyboardButton3.setText("Портфолио");
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

    public void showWorkTime(SendMessage sendMessage) {
        sendMessage.setText("Прием клиентов осуществляется " +
                "каждый понедельник и среду с 19:00 до 23:00 " +
                "\uD83D\uDD52.\n \nЕсли у вас есть вопросы или вам " +
                "нужна дополнительная информация, не стесняйтесь " +
                "обращаться к нашему администратору 👨‍💻. " +
                "Мы всегда рады помочь! \uD83D\uDE0A");
    }

    public void onSignUp(SendMessage sendMessage) {
        boolean isAvvailable = false;

        if (!isAvvailable) {

            sendMessage.setText("Пожалуйста, выберите подходящую для вас дату");

            Calendar calendar = new GregorianCalendar();
            DateFormat df = new SimpleDateFormat("E, d MMM yyy");

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton firstAvvailable = new InlineKeyboardButton();
            firstAvvailable.setText(df.format(calendar.getTime()));
            firstAvvailable.setCallbackData("queueForFirstDay");
            calendar.roll(Calendar.DAY_OF_WEEK, +2);
            InlineKeyboardButton secondAvvailable = new InlineKeyboardButton();
            secondAvvailable.setText(df.format(calendar.getTime()));
            secondAvvailable.setCallbackData("queueForFirstDay");
            rowInline.add(firstAvvailable);
            rowInline.add(secondAvvailable);

            markupInline.setKeyboard(Collections.singletonList(rowInline));
            sendMessage.setReplyMarkup(markupInline);

        } else if (!isAvvailable) {
            sendMessage.setText("К сожалению, все места уже заняты \uD83D\uDE14. Но не переживайте! " +
                    "Вы можете встать в очередь и мы обязательно найдем для вас время. Как вам такая идея? \uD83D\uDD52");

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();


            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton setQueue = new InlineKeyboardButton();
            setQueue.setText("Стать в очередь");
            setQueue.setCallbackData("put_person_in_queue");

            rowInline.add(setQueue);
            markupInline.setKeyboard(Collections.singletonList(rowInline));
            sendMessage.setReplyMarkup(markupInline);

        }
    }

    public void showRegisterMenu(SendMessage sendMessage) {
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
