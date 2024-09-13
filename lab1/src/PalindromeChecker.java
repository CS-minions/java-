import java.math.BigInteger;
import java.util.Random;

public class PalindromeChecker {
    public static void main(String[] args) {
        // 创建一个Random对象，用于生成随机数
        Random random = new Random();

        // 生成5个随机的21位数
        for (int i = 0; i < 5; i++) {
            // 使用BigInteger生成一个21位的随机整数
            BigInteger randomNumber = new BigInteger(210, random).abs();  // 生成一个长度为210位的随机数，确保是正数

            // 获取随机数的字符串表示
            String randomString = randomNumber.toString();

            // 计算逆序数
            String reversedString = new StringBuilder(randomString).reverse().toString();

            // 判断是否是回文
            boolean isPalindrome = randomString.equals(reversedString);

            // 输出结果
            System.out.println("随机数: " + randomString);
            System.out.println("逆序数: " + reversedString);
            System.out.println("是否是回文: " + (isPalindrome ? "是" : "否"));
            System.out.println("---------------------------------------");
        }
    }
}
