import java.util.Scanner; // 导入Scanner类，用于从键盘获取输入

public class SwapArrayElements {
    public static void main(String[] args) {
        // 创建一个Scanner对象，用于读取用户输入
        Scanner scanner = new Scanner(System.in);

        // 创建一个长度为21的浮点数组
        float[] numbers = new float[21];

        // 从键盘输入21个浮点数，并将其存储在数组中
        System.out.println("请输入21个浮点数：");
        for (int i = 0; i < numbers.length; i++) {
            System.out.print("第" + (i + 1) + "个数: ");
            numbers[i] = scanner.nextFloat(); // 读取浮点数并存入数组
        }

        // 打印原始数组
        System.out.println("原始数组：");
        printArray(numbers); // 调用printArray方法打印数组内容

        // 交换前5个元素与后5个元素
        for (int i = 0; i < 5; i++) {
            // 交换第i个元素和倒数第i个元素
            float temp = numbers[i]; // 将第i个元素存入临时变量
            numbers[i] = numbers[numbers.length - 1 - i]; // 将倒数第i个元素赋值给第i个位置
            numbers[numbers.length - 1 - i] = temp; // 将临时变量中的值赋值给倒数第i个位置
        }

        // 打印交换后的数组
        System.out.println("交换后的数组：");
        printArray(numbers); // 调用printArray方法打印数组内容

        // 关闭Scanner对象
        scanner.close();
    }

    // 打印数组内容的方法
    private static void printArray(float[] array) {
        for (float num : array) {
            System.out.print(num + " "); // 打印每个数组元素并用空格分隔
        }
        System.out.println(); // 换行
    }
}
