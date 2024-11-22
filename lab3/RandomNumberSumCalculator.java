import java.util.ArrayList;
import java.util.List;

public class RandomNumberSumCalculator {
    public static void main(String[] args) {
        SharedData sharedData = new SharedData();

        // 创建生成随机数的线程
        Thread producerThread = new Thread(new RandomNumberProducer(sharedData));
        producerThread.setName("Producer");

        // 创建计算和与平均值的线程
        Thread consumerThread = new Thread(new RandomNumberConsumer(sharedData));
        consumerThread.setName("Consumer");

        // 启动线程
        producerThread.start();
        consumerThread.start();
    }
}

class SharedData {
    private final List<Float> numbers = new ArrayList<>();
    private boolean isReady = false;

    public synchronized void addNumber(float number) {
        numbers.add(number);
        System.out.printf("Generated: %.4f\n", number);

        if (numbers.size() % 5 == 0) {
            isReady = true;
            notifyAll(); // 通知消费者线程进行计算
        }
    }

    public synchronized List<Float> getNumbers() {
        while (!isReady) {
            try {
                wait(); // 等待生成足够的数据
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer thread interrupted");
            }
        }

        // 复制前5个元素用于计算，并从原始列表中移除
        List<Float> subList = new ArrayList<>(numbers.subList(0, 5));
        numbers.subList(0, 5).clear();
        isReady = false;
        notifyAll(); // 通知生产者线程继续生成数据
        return subList;
    }
}

class RandomNumberProducer implements Runnable {
    private final SharedData sharedData;

    public RandomNumberProducer(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public void run() {
        while (true) {
            float randomNum = (float) Math.random(); // 生成 [0, 1) 的随机浮点数
            synchronized (sharedData) {
                sharedData.addNumber(randomNum);
            }
            try {
                Thread.sleep(200); // 模拟一些延迟
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer thread interrupted");
            }
        }
    }
}

class RandomNumberConsumer implements Runnable {
    private final SharedData sharedData;

    public RandomNumberConsumer(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public void run() {
        while (true) {
            List<Float> numbers = sharedData.getNumbers(); // 获取生成的5个随机数
            float sum = 0;
            for (float num : numbers) {
                sum += num;
            }
            float average = sum / numbers.size();
            System.out.printf("Sum: %.4f, Average: %.4f\n", sum, average);
        }
    }
}
