package org.example.bot.config;

import org.example.bot.dao.PersonDAO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class Logic_realisation {


    byte mondayValueIndicator = 5;


    public byte getMondayValueIndicator() {
        return mondayValueIndicator;
    }

    public void setMondayValueIndicator(byte mondayValueIndicator) {
        this.mondayValueIndicator = mondayValueIndicator;
    }

    byte wednesdayValueIndicator = 5;


    public byte getWednesdayValueIndicator() {
        return wednesdayValueIndicator;
    }

    public void setWednesdayValueIndicator(byte wednesdayValueIndicator) {
        this.wednesdayValueIndicator = wednesdayValueIndicator;
    }


    public void showHelloMessage(SendMessage sendMessage) {
        sendMessage.setText("Добро пожаловать в нашу парикмахерскую!\n" +
                "Мы рады видеть вас и готовы помочь вам записаться на услуги парикмахера");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline_1 = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Записаться");
        inlineKeyboardButton1.setCallbackData("signup");

        rowInline_1.add(inlineKeyboardButton1);


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

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Назад");
        inlineKeyboardButton1.setCallbackData("back");

        rowInline.add(inlineKeyboardButton1);

        markupInline.setKeyboard(Collections.singletonList(rowInline));
        sendMessage.setReplyMarkup(markupInline);
    }

    public void onSignUp(SendMessage sendMessage, PersonDAO dao, long chatId) {

        if (dao.getBookedForMonday().containsKey(chatId)) {
            sendMessage.setText("Вы уже записаны на понедельник!");

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();

            inlineKeyboardButton1.setText("Назад");
            inlineKeyboardButton1.setCallbackData("back");
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton2.setText("Покинуть очередь");
            inlineKeyboardButton2.setCallbackData("passMonday");

            rowInline.add(inlineKeyboardButton1);
            rowInline.add(inlineKeyboardButton2);

            markupInline.setKeyboard(Collections.singletonList(rowInline));
            sendMessage.setReplyMarkup(markupInline);
        }
        else if (dao.getBookedForWednesday().containsKey(chatId)) {
            sendMessage.setText("Вы уже записаны на среду!");

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();

            inlineKeyboardButton1.setText("Назад");
            inlineKeyboardButton1.setCallbackData("back");
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton2.setText("Покинуть очередь");
            inlineKeyboardButton2.setCallbackData("passWednesday");

            rowInline.add(inlineKeyboardButton1);
            rowInline.add(inlineKeyboardButton2);

            markupInline.setKeyboard(Collections.singletonList(rowInline));
            sendMessage.setReplyMarkup(markupInline);
        }




        else if ((dao.getMondaySize() < getMondayValueIndicator()) || (dao.getWednesdaySize() < getMondayValueIndicator())) {

            Calendar calendar = Calendar.getInstance();
            int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int daysUntilWednesday = (Calendar.WEDNESDAY - currentDayOfWeek + 7) % 7;
            if (daysUntilWednesday == 0 && currentHour >= 23) {
                daysUntilWednesday = 7;
                setWednesdayValueIndicator((byte) 5);
            }
            Calendar nextWednesday = (Calendar) calendar.clone();
            nextWednesday.add(Calendar.DAY_OF_MONTH, daysUntilWednesday);

            int daysUntilMonday = (Calendar.MONDAY - currentDayOfWeek + 7) % 7;
            if (daysUntilMonday == 0 && currentHour >= 23) {
                daysUntilMonday = 7;
                setMondayValueIndicator((byte) 5);
            }
            Calendar nextMonday = (Calendar) calendar.clone();
            nextMonday.add(Calendar.DAY_OF_MONTH, daysUntilMonday);

            sendMessage.setText("Пожалуйста, выберите подходящую для вас дату");


            DateFormat df = new SimpleDateFormat("E, d MMM yyy");

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

            List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
            List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

            InlineKeyboardButton firstAvvailable = new InlineKeyboardButton();
            if (getMondayValueIndicator() - dao.getMondaySize() > 0) {
                firstAvvailable.setText(df.format(nextMonday.getTime()) + (", доступно мест: " + (getMondayValueIndicator() - dao.getMondaySize())));
                firstAvvailable.setCallbackData("bookForMonday");

            } else {
                firstAvvailable.setText(df.format(calendar.getTime()) + ", свободных мест нет");
                firstAvvailable.setCallbackData("no_room");
            }

            InlineKeyboardButton secondAvvailable = new InlineKeyboardButton();


            if (getWednesdayValueIndicator() - dao.getWednesdaySize() > 0) {
                secondAvvailable.setText(df.format(nextWednesday.getTime()) + (", доступно мест: " + (getWednesdayValueIndicator() - dao.getWednesdaySize())));
                secondAvvailable.setCallbackData("bookForWednesday");
            } else {
                secondAvvailable.setText(df.format(calendar.getTime()) + ", свободных мест нет");
                secondAvvailable.setCallbackData("no_room");
            }


            rowInline1.add(firstAvvailable);
            rowInline2.add(secondAvvailable);
            rowsInline.add(rowInline1);
            rowsInline.add(rowInline2);
            markupInline.setKeyboard(rowsInline);
            sendMessage.setReplyMarkup(markupInline);

        } else {
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


}
