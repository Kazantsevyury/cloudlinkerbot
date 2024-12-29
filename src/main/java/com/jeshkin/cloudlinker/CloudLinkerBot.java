package com.jeshkin.cloudlinker;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;


public class CloudLinkerBot extends TelegramLongPollingBot {
    private final String token;
    private final String username;

    public CloudLinkerBot(String token, String username) {
        this.token = token;
        this.username = username;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }



    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if ("/localip".equalsIgnoreCase(messageText)) {
                try {
                    InetAddress localIp = InetAddress.getLocalHost();
                    sendText(chatId, "Ваш локальный IP: " + localIp.getHostAddress());
                } catch (UnknownHostException e) {
                    sendText(chatId, "Не удалось получить локальный IP. Ошибка: " + e.getMessage());
                    e.printStackTrace();
                }
            } else if ("/publicip".equalsIgnoreCase(messageText)) {
                try {
                    String publicIp = getPublicIp();
                    sendText(chatId, "Ваш публичный IP: " + publicIp);
                } catch (Exception e) {
                    sendText(chatId, "Не удалось получить публичный IP. Ошибка: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                sendText(chatId, "Неизвестная команда");
            }
        }
    }

    private String getPublicIp() throws Exception {
        URL url = new URL("https://api64.ipify.org"); // Простой сервис для получения публичного IP
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return in.readLine();
        }
    }



    private void sendText(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
