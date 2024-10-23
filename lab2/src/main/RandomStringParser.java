package main;

import java.util.Random;

public class RandomStringParser {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            String randomString = generateRandomString(20);
            System.out.println("随机生成的字符串: " + randomString);
            parseAndPrint(randomString);
            System.out.println();
        }
    }

    // 随机生成包含大写字母、小写字母、数字和其他字符的字符串
    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789$@#%^&*()_+!";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    // 解析并按顺序输出大写字母、小写字母、数字和其他字符
    private static void parseAndPrint(String input) {
        StringBuilder uppercaseLetters = new StringBuilder();
        StringBuilder lowercaseLetters = new StringBuilder();
        StringBuilder digits = new StringBuilder();
        StringBuilder specialChars = new StringBuilder();

        for (char ch : input.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                uppercaseLetters.append(ch);
            } else if (Character.isLowerCase(ch)) {
                lowercaseLetters.append(ch);
            } else if (Character.isDigit(ch)) {
                digits.append(ch);
            } else {
                specialChars.append(ch);
            }
        }

        System.out.println("大写字母: " + uppercaseLetters);
        System.out.println("小写字母: " + lowercaseLetters);
        System.out.println("数字: " + digits);
        System.out.println("其他字符: " + specialChars);
    }
}
