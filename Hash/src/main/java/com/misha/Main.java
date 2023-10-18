package com.misha;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main
{
    public static void main( String[] args )
    {
        String message = "Hello, 2";
        System.out.println(message);
        MD5 md5 = new MD5(message);

        byte[] paddedMessage = md5.padMessage();

        String hexString = bytesToHexString(paddedMessage);
        System.out.println(hexString);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
