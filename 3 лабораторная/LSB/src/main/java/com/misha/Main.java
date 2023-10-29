package com.misha;

public class Main
{
    public static void main( String[] args )
    {
        try {
            //шифруемое послание
            String message = "This is a secret message!";

            Steganography steganography = new Steganography();

            //шифрование текста в изображение winter.bmp
            steganography.hideMessage("winter.bmp", "stego.bmp", message);

            //считываем зашифрованный файл и получаем сообщение
            String extractedMessage = steganography.extractMessage("stego.bmp");
            System.out.println("Decode message: " + extractedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
