package com.misha;
import org.bouncycastle.crypto.engines.SkipjackEngine;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class Encrypt {
    private String filePath;
    private ArrayList<String> textEncrypt;
    private String fileEncryptPath;
    private byte key[];

    public String getFileEncryptPath() {
        return fileEncryptPath;
    }

    public Encrypt(String filePath){
        this.filePath = filePath;

        //создаем переменную, которая будет хранить весь зашифрованный текст
        textEncrypt = new ArrayList<>();

        //инициализируем ключем с длинной 10, так как алгоритм Skipkack требует такой длинны
        initKey();
        //шифруем входной текст
        encrypt();
    }

    //инициализируем рандомной ключ длинны 10
    private void initKey(){
        Random random = new Random();
        int randomNumber;
        key = new byte[10];
        for(int i = 0; i < 10; i += 1) {
            randomNumber = random.nextInt(256);
            key[i] = (byte) randomNumber;
        }

    }

    //шифруем входящий текст
    private void encrypt(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            //инициализация класса шифрования
            SkipjackEngine engine = new SkipjackEngine();

            KeyParameter param = new KeyParameter(key);

            engine.init(true, param);

            String line;
            ArrayList<String> substr8 = new ArrayList<>();

            //в цикле считываем файл пока не дойдем до конца
            while ((line = reader.readLine()) != null) {

                //делим входную строчку на 8 символов, то есть на 8 байт, так как алгоритм принимает на вход блок из 8 байтов
                substr8 = splitStringByLength(line, 8);
                for(String split : substr8) {
                    int split_length = split.length();

                    //если не хватает символов до 8, то заполняем пробелами
                    if(split_length < 8){
                        for(int i = 0; i < 8 - split_length; i += 1){
                            split = split + " ";
                        }
                    }

                    byte[] encrypted = new byte[split.length()];
                    engine.processBlock(split.getBytes(), 0, encrypted, 0);

                    textEncrypt.add(toStringByteArr(encrypted));
                }
                textEncrypt.add(new String("\n"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toStringByteArr(byte[] bytes){

        String str = "";

        for(int i = 0; i < bytes.length; i += 1){
            str = str + Byte.toString(bytes[i]) + " ";
        }

        return str;
    }
    private ArrayList<String> splitStringByLength(String input, int length) {
        ArrayList<String> substrings = new ArrayList<>();

        String substring;
        for (int i = 0; i < input.length(); i += length) {
            substring = input.substring(i, Math.min(i + length, input.length()));
            substrings.add(substring);
        }

        return substrings;
    }

    //записываем выходной файл
    public void createOutFile(){
        int positionPoint = filePath.lastIndexOf(".");

        if(positionPoint != -1) {
            fileEncryptPath = filePath.substring(0, positionPoint) + "Encrypt.txt";
        }
        else {
            fileEncryptPath = filePath;
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileEncryptPath));

            bufferedWriter.write("key:");
            for(int i = 0; i < key.length; i += 1){
                bufferedWriter.write(Byte.toString(key[i]) + " ");
            }
            bufferedWriter.write("\n");

            for (String line : textEncrypt) {
                bufferedWriter.write(line);
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
