package com.misha;

import java.util.Random;

public class Encrypt {
    private String message;
    private String messageEncrypt;
    private int length;
    private String key;
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxy";

    public String getKey(){
        return key;
    }

    public String getMessageEncrypt(){
        return messageEncrypt;
    }

    public Encrypt(String message){
        this.message = message;
        length = message.length();

        //инициализация ключа шифрования
        initKey();
        //шифрование
        encrypt();
    }

    //инициализация рандомного ключа с длинной равной длины сообщения
    private void initKey(){
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i += 1) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        key = randomString.toString();
    }

    //шифрование выполняется побитовой операцией xor
    private void encrypt(){
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            char messageChar = message.charAt(i);
            char keyChar = key.charAt(i);

            char encryptedChar = (char) (messageChar ^ keyChar);

            encrypted.append(encryptedChar);
        }

        messageEncrypt = encrypted.toString();
    }
}
