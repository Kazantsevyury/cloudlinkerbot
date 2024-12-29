package com.jeshkin.cloudlinker;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            // Загружаем конфигурацию
            Config config = Config.load("src/main/resources/application-secrets.yml");

            // Создаем и запускаем бота
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new CloudLinkerBot(config.getBot().getToken(), config.getBot().getUsername()));

            System.out.println("Бот запущен!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}