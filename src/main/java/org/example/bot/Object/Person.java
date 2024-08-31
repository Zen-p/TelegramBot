package org.example.bot.Object;

public class Person {
    private static long id;
    private String nameAndSurname;

    public Person() {

    }

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        Person.id = id;
    }

    public String getNameAndSurname() {
        return nameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }
}
