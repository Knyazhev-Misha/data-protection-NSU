package com.misha;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MD5 {
    private String message;
    private byte[] messageBytes;
    private int[] T;
    private int[] S;
    public MD5(String message){
        this.message = message;
        messageBytes = getBytes(message);
    }

    public byte[] padMessage() {
        int originalLength = messageBytes.length;
        int bitLength = originalLength * 8;

        // Шаг 1: Выравнивание потока
        int paddingLength = (448 - (bitLength)) % 512;
        if (paddingLength < 0) {
            paddingLength += 512;
        }
        paddingLength /= 8;

        byte[] paddedMessage = new byte[originalLength + paddingLength + 8];
        System.arraycopy(messageBytes, 0, paddedMessage, 0, originalLength);

        // Дописываем единичный бит
        paddedMessage[originalLength] = (byte) 0x80;

        // Шаг 2: Добавление длины сообщения (64-битное представление)
        byte[] lengthBytes = new byte[8];
        for (int i = 0; i < 8; i += 1) {
            lengthBytes[i] = (byte) ((bitLength >>> (i * 8)) & 0xFF);
        }

        int messageLength = originalLength + paddingLength;
        System.arraycopy(lengthBytes, 0, paddedMessage, messageLength, 8);

        //Шаг 3: Инициализация буфера
        int A = 0x67452301;  // 32-битное начальное значение A
        int B = 0xEFCDAB89;  // 32-битное начальное значение B
        int C = 0x98BADCFE;  // 32-битное начальное значение C
        int D = 0x10325476;  // 32-битное начальное значение D

        //Шаг 4: Вычисление в цикле
        //- создаем 4 функции: FunF, FunG, FunH, FunI
        //- создаем таблицу констант
        initT();
        //- инициализируем сдвиги S
        initS();
        //для каждых 512 бит
        for(int i = 0; i < paddedMessage.length; i += 64){
            byte[] message512 = splitByteByLength(paddedMessage, i, i + 64);

            //создаем X[15], где X[i] - это целое число полученное из 4 байт
            int[] X = new int[16];

            for(int t = 0; t < 16; t += 1){
                byte[] message4 = splitByteByLength(message512, t * 4, t * 4 + 4);
                X[t] = parseBytetoInt(message4);
            }

            //для обновления A, B, C, D
            int AA = A, BB = B, CC = C, DD = D;

            for(int q = 0; q < 64; q += 1){
                int F = 0, g = 0;

                if(0 <= q && q <= 15) {
                    F = (B & C) | ((~B) & D);
                    g = q;
                }

                else if(16 <= q && q <= 31) {
                    F =(D & B) | ((~D) & C);
                    g = (5 * q + 1) % 16;
                }

                else if(32 <= q && q <= 47) {
                    F = B ^ C ^ D;
                    g = (3 * q + 5) % 16;
                }

                else if(48 <= q && q <= 63)
                    F = C ^ (B ^ (~D));
                    g = (7 * q) % 16;
                F = F + A + T[i] + X[g];
                A = D;
                D = C;
                C = B;

                int shiftAmount = S[i]; // Количество бит для сдвига
                int value = F;
                int leftShifted = (value << shiftAmount) | (value >>> (32 - shiftAmount));

                B = B + leftShifted;
            }

            //обновляем A, B, C, D
            A += AA;
            B += BB;
            C += CC;
            D += DD;
        }

        byte[] hash = getMD5Hash(A,B, C, D);

        return hash;
    }

    private byte[] getBytes(String message){
        byte[] b = new byte[message.length()];
        for (int i = 0; i < message.length(); i += 1) {
            b[i] = (byte) message.charAt(i);
        }
        return b;
    }

    // Функция FunF
    private int funF(int X, int Y, int Z) {
        return (X & Y) | (~X & Z);
    }

    // Функция FunG
    private int funG(int X, int Y, int Z) {
        return (X & Z) | (~Z & Y);
    }

    // Функция FunH
    private int funH(int X, int Y, int Z) {
        return X ^ Y ^ Z;
    }

    // Функция FunI
    private int funI(int X, int Y, int Z) {
        return Y ^ (~Z | X);
    }

    private void initT(){
        T = new int[64];

        for (int n = 1; n <= 64; n++) {
            double sinValue = Math.sin(n);
            long intValue = (long) (Math.pow(2, 32) * Math.abs(sinValue));
            T[n - 1] = (int) intValue;
        }
    }
    private void initS(){
        S = new int[]{ 7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,
                       5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,
                       4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,
                       6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21 };
    }

    private byte[] splitByteByLength(byte[] bytes, int start, int end) {
        byte[] ret = new byte[end - start];

        for (int i = start; i < end; i += 1) {
            ret[i - start] = bytes[i];
        }

        return ret;
    }

    private int parseBytetoInt(byte[] b){
        int a = 0;
        for (int i = 0; i < b.length; i++) {
            a = (a << 8) | (b[i] & 0xFF);
        }
        return a;
    }

    private byte[] getMD5Hash(int A, int B, int C, int D) {
        byte[] hash = new byte[16]; // 128 бит хеш = 16 байт

        // Объединение значений A, B, C, и D
        long combined = ((long) A & 0xFFFFFFFFL) |
                (((long) B & 0xFFFFFFFFL) << 32) |
                (((long) C & 0xFFFFFFFFL) << 64) |
                (((long) D & 0xFFFFFFFFL) << 96);

        // Разделение на байты
        for (int i = 0; i < 16; i++) {
            hash[i] = (byte) (combined & 0xFF);
            combined >>= 8;
        }

        return hash;
    }

}

