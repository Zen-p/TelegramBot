package org.example.bot.dao;

import org.example.bot.Object.Person;
import org.example.bot.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;


public class PersonDAO {

    private long chatId;

    private int serialIdForMonday;

    public int getSerialIdForMonday() {
        return serialIdForMonday;
    }

    public void setSerialIdForMonday(int serialIdForMonday) {
        this.serialIdForMonday = serialIdForMonday;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    List<Person> bookedForMonday;
    List<Person> bookedForWednesday;

    {
        bookedForMonday = new LinkedList<>();
        bookedForWednesday = new LinkedList<>();

        bookedForMonday.add(new Person(39846349867l, "Peter Fridge"));
        bookedForMonday.add(new Person(34658734563l, "Matvei Macevich"));
        bookedForMonday.add(new Person(43653483468l, "Egor Karpovich"));
        bookedForMonday.add(new Person(37465438755l, "kirill Shirko"));


        bookedForWednesday.add(new Person(38456834454l, "Egor Kluch"));
        bookedForWednesday.add(new Person(34567875647l, "Egor Shishko"));
        bookedForWednesday.add(new Person(90873458745l, "Narbut Alex"));
        bookedForWednesday.add(new Person(76276345867l, "kirill Manul"));

    }


    public void addNewUserForMonday(Person person) {
        bookedForMonday.add(person);
        System.out.println("Записался на понедельник");
        person.setSerialNumber(1 + getSerialIdForMonday());

    }


    public int getMondaySize() {
        return bookedForMonday.size();
    }

    public int getWednesdaySize() {
        return bookedForWednesday.size();
    }

    public void seeTheQueue(SendMessage sendMessage, TelegramBot bot) {
        for (Person p : bookedForMonday) {
            sendMessage.setText(p.toString());
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }
    }


}
