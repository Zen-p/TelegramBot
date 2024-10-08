package org.example.bot.Object;

public class Person {

    private long chatId;
    private String telegramUsername;
    private int serialNumber;
    private String nameAndSurname;

    public Person() {
    }

    public Person(String telegramUsername, long chatId, String nameAndSurname) {
        this.telegramUsername = telegramUsername;
        this.serialNumber = serialNumber;
        this.chatId = chatId;
        this.nameAndSurname = nameAndSurname;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNameAndSurname() {
        return nameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }

    @Override
    public String toString() {
        return ("telegram Username: @" + this.getTelegramUsername() + "\nserialNumber: " + this.getSerialNumber() + "\n" +
                "chat id: " + chatId + "\nname and surname: " + nameAndSurname + "\n___________________________________");
    }

    public String toStringForAdmin() {
        return ("Ник в телеграм: @" + this.getTelegramUsername() + "\nИмя и фамилия: " + nameAndSurname + "\n___________________________________");
    }
}
