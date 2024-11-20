class Left extends Thread {
    int n = 0;  // 计数器

    // 重写run方法，线程执行的任务
    public void run() {
        while(true) {  // 无限循环
            n++;  // 每次循环计数器加1
            System.out.println(n + " Left");  // 输出计数值和"Left"
            try {
                // 让线程休眠一个随机时间，单位是毫秒
                sleep((int)(Math.random() * 100));
            } catch (InterruptedException e) {
                // 如果线程被中断，捕获并处理异常
            }
        }
    }
}
class Right extends Thread {
    int n = 0;  // 计数器

    // 重写run方法，线程执行的任务
    public void run() {
        while(true) {  // 无限循环
            n++;  // 每次循环计数器加1
            System.out.println(n + " Right");  // 输出计数值和"Right"
            try {
                // 让线程休眠一个随机时间，单位是毫秒
                sleep((int)(Math.random() * 100));
            } catch (InterruptedException e) {
                // 如果线程被中断，捕获并处理异常
            }
        }
    }
}
public class Example8_3 {
    public static void main(String args[]) {
        Left left = new Left();  // 创建左边线程
        Right right = new Right();  // 创建右边线程

        left.start();  // 启动左边线程
        right.start();  // 启动右边线程

        while(true) {  // 无限循环，直到满足终止条件
            try {
                Thread.sleep(100);  // 主线程每100毫秒休眠一次，允许子线程继续运行
            } catch (InterruptedException e) {
                e.printStackTrace();  // 捕获并打印异常
            }

            // 如果任意一个线程的计数器n达到或超过8，则退出程序
            if(left.n >= 8 || right.n >= 8) {
                System.out.println(left.n + "," + right.n);  // 输出当前两个线程的计数值
                System.exit(0);  // 退出程序
            }
        }
    }
}
