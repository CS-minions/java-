class Task_fixed implements Runnable {
    private int number = 0;

    // 设置数字初值
    public void setNumber(int n) {
        number = n;
    }

    // 线程执行的方法
    public void run() {
        while (true) {
            if (Thread.currentThread().getName().equals("add")) {
                synchronized (this) {
                    // 如果是加法线程
                    number++;
                    System.out.printf("%d\n", number);  // 输出当前值
                }
            } else if (Thread.currentThread().getName().equals("sub")) {
                synchronized (this) {
                    // 如果是减法线程
                    number--;
                    System.out.printf("%12d\n", number);  // 输出当前值
                }
            }
            try {
                Thread.sleep(1000);  // 休眠1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Example8_5_fixed {
    public static void main(String[] args) {
        Task taskAdd = new Task();
        taskAdd.setNumber(10);  // 设置加法线程初值为10

        Task taskSub = new Task();
        taskSub.setNumber(-10); // 设置减法线程初值为-10

        // 创建线程
        Thread threadA, threadB, threadC, threadD;

        threadA = new Thread(taskAdd);
        threadB = new Thread(taskAdd);
        threadA.setName("add");
        threadB.setName("add");

        threadC = new Thread(taskSub);
        threadD = new Thread(taskSub);
        threadC.setName("sub");
        threadD.setName("sub");

        // 启动线程
        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
    }
}
