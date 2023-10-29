package com.misha;

import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Steganography {
    public void hideMessage(String imagePath, String outputPath, String message) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));

        //конец шифрования
        message = message + "@@@END@@@";
        int messageLength = message.length();
        int width = image.getWidth();
        int height = image.getHeight();

        //проверяем что можем зашифровать сообщение
        if (messageLength * 8 > width * height) {
            throw new IllegalArgumentException("The message is too long to hide in this image.");
        }

        int pixelIndex = 0;
        for (int i = 0; i < messageLength; i++) {
            char c = message.charAt(i);
            for (int j = 7; j >= 0; j--) {
                //считываем каждый бит символа
                int bit = (c >> j) & 1;
                //считываем ргб изобраения
                int pixel = image.getRGB(pixelIndex % width, pixelIndex / width);
                //в каждый пиксель в его начала ложим бит сообщения
                pixel = (pixel & 0xFFFFFFFE);
                pixel = pixel | bit; // Hide the bit in the alpha channel

                image.setRGB(pixelIndex % width, pixelIndex / width, pixel);
                pixelIndex++;
            }
        }

        File outputFile = new File(outputPath);
        ImageIO.write(image, "BMP", outputFile);
    }

    public String extractMessage(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));

        int width = image.getWidth();
        int height = image.getHeight();

        int messageLength = 0;
        int pixelIndex = 0;
        int[] buff = new int[8];
        StringBuilder message = new StringBuilder();

        int i = 1;
        //считываем каждый бит зашифрованного сообщения пока не наткнемся на конец сообщения
        while (message.toString().contains("@@@END@@@") == false && i < width * height){
            int pixel = image.getRGB(pixelIndex % width, pixelIndex / width);
            int bit = pixel & 1;
            //заполняем буфер символа
            buff[(i - 1) % 8] = bit;
            pixelIndex += 1;

            //8 бит образуют 1 символ
            if(i % 8 == 0){
                message.append(getChar(buff));
            }

            i += 1;
        }

        return message.toString().replace("@@@END@@@", "");
    }

    private char getChar(int[] arr){
        int symb = 0;
        for(int i = 0; i < 8; i += 1){
            symb = symb << 1;
            symb = symb | arr[i];
        }
        return (char)symb;
    }
}
