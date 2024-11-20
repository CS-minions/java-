// 继承Thread类创建一个自定义线程类
class WriteWordThread extends Thread {
    int n = 0;  // 定义一个整数n，用来设置线程休眠的时间（单位：毫秒）

    // 构造方法，接受线程名称和休眠时间n
    WriteWordThread(String s, int n) {
        setName(s);  // 设置线程的名称
        this.n = n;  // 设置休眠时间
    }

    // 重写run方法，定义线程的执行任务
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("Thread: " + getName());  // 打印当前线程的名称
            try {
                sleep(n);  // 让线程休眠n毫秒，模拟耗时操作
            } catch (InterruptedException e) {
                // 如果线程被中断，捕获异常
            }
        }
    }
}

public class Example8_2 {
    public static void main(String args[]) {
        // 创建两个线程对象，并指定线程名称和休眠时间
        WriteWordThread zhang, wang;
        zhang = new WriteWordThread("Zhang", 200);  // 线程Zhang休眠200毫秒
        wang = new WriteWordThread("Wang", 100);   // 线程Wang休眠100毫秒

        // 启动两个线程
        zhang.start();
        wang.start();
    }
}
