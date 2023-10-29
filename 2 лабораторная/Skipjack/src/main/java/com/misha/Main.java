package com.misha;

import org.bouncycastle.crypto.engines.SkipjackEngine;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Main
{
    public static void main( String[] args ) throws UnsupportedEncodingException {

        //входной файл с текстом
        String file = "in.txt";
        //инициализация класса шифрования
        Encrypt encrypt = new Encrypt(file);
        encrypt.createOutFile();

        //пробуем инициалиализировать класс дешифрования, если не будет ключевого слова: key, то конструктор класса выдаст ошибку
        try {
            Decrypt decrypt = new Decrypt(encrypt.getFileEncryptPath());
            decrypt.createOutFile();
        }
        catch (KeyNotFoundException e){
            e.printStackTrace();
        }
    }
}
