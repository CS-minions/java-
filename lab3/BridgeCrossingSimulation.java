import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BridgeCrossingSimulation {
    private static final int NUM_PEOPLE = 1000;
    private static final Object bridgeLock = new Object(); // 桥的同步锁
    private static AtomicInteger southCount = new AtomicInteger(0);
    private static AtomicInteger northCount = new AtomicInteger(0);

    public static void main(String[] args) {
        int southWins = 0;
        int northWins = 0;

        for (int run = 1; run <= 10; run++) {
            // 重置计数器
            southCount.set(0);
            northCount.set(0);

            // 创建南边和北边的人线程
            List<Thread> threads = new ArrayList<>();
            for (int i = 1; i <= NUM_PEOPLE; i++) {
                threads.add(new Thread(new Person("S" + i, "South")));
                threads.add(new Thread(new Person("N" + i, "North")));
            }

            // 启动所有线程
            for (Thread thread : threads) {
                thread.start();
            }

            // 等待所有线程完成
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted: " + e.getMessage());
                }
            }

            // 判断哪一侧先完成
            if (southCount.get() == NUM_PEOPLE) {
                southWins++;
                System.out.printf("第%d次运行，南边先完成过桥。\n", run);
            } else if (northCount.get() == NUM_PEOPLE) {
                northWins++;
                System.out.printf("第%d次运行，北边先完成过桥。\n", run);
            }
        }

        // 输出最终统计结果
        System.out.println("南边先全部完成的次数: " + southWins);
        System.out.println("北边先全部完成的次数: " + northWins);
    }

    static class Person implements Runnable {
        private final String name;
        private final String direction;

        public Person(String name, String direction) {
            this.name = name;
            this.direction = direction;
        }

        @Override
        public void run() {
            try {
                // 每个人花费随机时间到达桥口
                Thread.sleep((int) (Math.random() * 100));
                // 过桥过程
                synchronized (bridgeLock) {
                    System.out.println(name + " from " + direction + " is crossing the bridge...");
                    Thread.sleep(10); // 模拟过桥时间
                }

                // 统计过桥人数
                if (direction.equals("South")) {
                    southCount.incrementAndGet();
                } else if (direction.equals("North")) {
                    northCount.incrementAndGet();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }
    }
}
