package com.example.traveling_app.common;

import java.nio.charset.Charset;
import java.util.Random;

public class RandomValue {
    public static String generateRandomString(int n) {
        byte[] array = new byte[256];
        new java.util.Random().nextBytes(array);
        String randomString = new String(array, Charset.forName("UTF-8"));
        StringBuffer r = new StringBuffer();
        for (int k = 0; k < randomString.length(); k++) {
            char ch = randomString.charAt(k);
            if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) && (n > 0)) {
                r.append(ch);
                n--;
            }
        }
        return r.toString();
    }

    public static String generateRandomNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Độ dài phải là một số nguyên dương.");
        }
        Random random = new Random();
        StringBuilder randomStringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Sinh số ngẫu nhiên từ 0 đến 9
            randomStringBuilder.append(digit);
        }
        return randomStringBuilder.toString();
    }
}
