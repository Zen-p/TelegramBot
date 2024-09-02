package org.example.bot.Object;

public class Person {
    private static long chatId;
    private String nameAndSurname;

    public Person(long id, String nameAndSurname) {
        this.chatId = id;
        this.nameAndSurname = nameAndSurname;
    }

    public static long getId() {
        return chatId;
    }

    public static void setId(long id) {
        Person.chatId = id;
    }

    public String getNameAndSurname() {
        return nameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }
}
