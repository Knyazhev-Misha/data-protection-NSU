package com.misha;

import java.util.Scanner;

public class Main
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);

        //считывание входной строки
        System.out.print("Введите сообщение: ");
        String message = scanner.nextLine();

        //инициализация класса шифрования
        Encrypt encrypt = new Encrypt(message);
        System.out.println("Зашифрованное сообщение: " + encrypt.getMessageEncrypt());
        System.out.println("Ключ шифрования: " + encrypt.getKey());

        //инициализация класса дешифрования
        Decrypt decrypt = new Decrypt(encrypt.getMessageEncrypt(), encrypt.getKey());
        System.out.println("Расшифрованное сообщение: " + decrypt.getMessageDecrypt());
    }
}
