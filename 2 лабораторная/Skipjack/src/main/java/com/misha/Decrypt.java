package com.misha;
import org.bouncycastle.crypto.engines.SkipjackEngine;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Decrypt {
    private String filePath;
    private ArrayList<String> textDecrypt;
    private String fileDecryptPath;
    private byte key[];

    public String getFileEncryptPath() {
        return fileDecryptPath;
    }

    public Decrypt(String filePath){
        this.filePath = filePath;

        //переменная для хранения расшифрованного файла
        textDecrypt = new ArrayList<>();

        //считывания ключа с проверкой его существования
        if(initKey()) {
            decrypt();
        }
        else{
            throw new KeyNotFoundException();
        }
    }

    private boolean initKey() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line = reader.readLine();
            reader.close();

            String check = line.substring(0, "key:".length());
            String key = line.substring("key:".length(), line.length());

            if(check.equals("key:") && key.length() != 0){
                String[] keyArr= key.split(" ");
                this.key = new byte[keyArr.length];
                for(int i = 0; i < keyArr.length; i += 1) {
                    this.key[i] = Byte.parseByte(keyArr[i]);
                }
                return true;
            }
            else {
                return false;
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] toByteArrString(String string){

        String[] nums = string.split(" ");

        byte[] bytes = new byte[nums.length];

        for(int i = 0; i < bytes.length; i += 1){
            bytes[i] = Byte.parseByte(nums[i]);
        }

        return bytes;
    }

        private void decrypt(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            SkipjackEngine engine = new SkipjackEngine();

            KeyParameter param = new KeyParameter(key);

            engine.init(false, param);

            String line = reader.readLine();
            ArrayList<String> substr8 = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if(!line.equals("")) {
                    byte[] bytes = toByteArrString(line);
                    for (int i = 0; i < bytes.length; i += 8) {

                        byte[] encrypted = new byte[8];
                        //проводим расшифрования только для 8 байт
                        engine.processBlock(splitByteByLength(bytes, i, i + 8), 0, encrypted, 0);

                        textDecrypt.add(new String(encrypted, StandardCharsets.UTF_8));
                    }
                }
                textDecrypt.add(new String("\n"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] splitByteByLength(byte[] bytes, int start, int end) {
        byte[] ret = new byte[end - start];

        for (int i = start; i < end; i += 1) {
            ret[i - start] = bytes[i];
        }

        return ret;
    }

    //вывести в файл драсшифрованные данные
    public void createOutFile(){
        int positionPoint = filePath.lastIndexOf(".");
        if(positionPoint != -1) {
            filePath = filePath.substring(0, positionPoint) + "Decrypt.txt";
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));

            for (String line : textDecrypt) {
                bufferedWriter.write(line);
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
