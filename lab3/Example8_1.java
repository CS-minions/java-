// 定义一个继承自Thread类的类
 class WriteWordThread2 extends Thread {
    WriteWordThread2(String s) {
        setName(s);  // 设置线程的名字
    }

    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("Thread: " + getName());  // 打印当前线程的名称
        }
    }
}

public class Example8_1 {
    public static void main(String args[]) {
        WriteWordThread2 zhang, wang;
        zhang = new WriteWordThread2("Zhang");  // 创建线程Zhang
        wang = new WriteWordThread2("Wang");  // 创建线程Wang

        zhang.start();  // 启动线程Zhang
        for (int i = 1; i <= 3; i++) {
            System.out.println("Main Thread");  // 主线程打印信息
        }
        wang.start();  // 启动线程Wang
    }
}
