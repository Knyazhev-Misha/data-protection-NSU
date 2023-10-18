package com.misha;

public class Decrypt {
    private String message;
    private String messageDecrypt;
    private int length;
    private String key;

    public String getMessageDecrypt(){
        return messageDecrypt;
    }

    //получаем созданный ключ шифрования из класса Encrypt
    public Decrypt(String message, String key){
        this.message = message;
        length = message.length();
        this.key = key;

        decrypt();
    }

    //для дешифрования надо также провести побитовую операцию с ключем шифрования
    private void decrypt(){
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            char messageChar = message.charAt(i);
            char keyChar = key.charAt(i);

            char encryptedChar = (char) (messageChar ^ keyChar);

            encrypted.append(encryptedChar);
        }

        messageDecrypt = encrypted.toString();
    }

}
