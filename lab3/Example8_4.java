class TaskBank implements Runnable {
    private int money = 0;
    String name1, name2;

    // 构造函数，用于设置两个线程的名称
    TaskBank(String s1, String s2) {
        name1 = s1;
        name2 = s2;
    }

    // 设置初始金额
    public void setMoney(int amount) {
        money = amount;
    }

    // 线程执行的任务
    public void run() {
        while (true) {
            money = money - 10; // 每次扣除 10 单位
            if (Thread.currentThread().getName().equals(name1)) {
                // 如果是“会计”线程
                System.out.println(name1 + ": " + money);
                if (money <= 100) {
                    System.out.println(name1 + ": Finished");
                    return;  // 会计完成任务后结束线程
                }
            } else if (Thread.currentThread().getName().equals(name2)) {
                // 如果是“出纳”线程
                System.out.println(name2 + ": " + money);
                if (money <= 60) {
                    System.out.println(name2 + ": Finished");
                    return;  // 出纳完成任务后结束线程
                }
            }
            try {
                Thread.sleep(800);  // 每次操作后线程休眠 800 毫秒
            } catch (InterruptedException e) {}
        }
    }
}

public class Example8_4 {
    public static void main(String args[]) {
        String s1 = "treasurer zhang";  // 会计线程名称
        String s2 = "cashier cheng";    // 出纳线程名称

        TaskBank taskBank = new TaskBank(s1, s2);
        taskBank.setMoney(120);  // 设置初始余额为 120

        // 创建并启动两个线程
        Thread zhang;
        Thread cheng;

        zhang = new Thread(taskBank);  // 创建会计线程
        cheng = new Thread(taskBank);  // 创建出纳线程

        zhang.setName(s1);  // 设置线程名称
        cheng.setName(s2);  // 设置线程名称

        zhang.start();  // 启动会计线程
        cheng.start();  // 启动出纳线程
    }
}
