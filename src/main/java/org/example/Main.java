package org.example;

import org.example.bot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        System.out.println("Version: 1.3");
        TelegramBot bot = new TelegramBot();
        Thread checker = new Thread(new TimeChecker(bot));
        checker.start();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        telegramBotsApi.registerBot(new TelegramBot());
    }
}


class TimeChecker implements Runnable{
    TelegramBot bot;

    public TimeChecker(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void run () {
        bot.checkIfQueueAvailable();
    }
}