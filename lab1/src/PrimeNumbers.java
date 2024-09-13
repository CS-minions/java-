public class PrimeNumbers {
    public static void main(String[] args) {
        // 定义起始和结束范围
        int start = 10;
        int end = 10000;
        int primeCount = 0; // 记录素数的数量

        // 输出提示信息
        System.out.println("10到10000之间的素数有：");

        // 循环遍历10到10000之间的所有数字
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) { // 调用isPrime方法判断是否为素数
                System.out.print(i + " "); // 如果是素数则输出
                primeCount++; // 计数器加一
            }
        }

        // 换行以便显示总的素数数量
        System.out.println();
        // 输出总的素数数量
        System.out.println("总共有 " + primeCount + " 个素数。");
    }

    // 判断是否为素数的方法
    public static boolean isPrime(int num) {
        if (num < 2) {
            return false; // 小于2的数字不是素数
        }
        // 检查num是否能被2到num的平方根之间的任何数整除
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false; // 如果能被整除则不是素数
            }
        }
        return true; // 否则是素数
    }
}
