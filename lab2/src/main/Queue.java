package main;

public class Queue {
    private double[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 32;

    // 构造方法，初始化队列容量为32
    public Queue() {
        elements = new double[DEFAULT_CAPACITY];
        size = 0;
    }

    // 往队列中添加一个double型数据
    public void enQueue(double v) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full");
        }
        elements[size++] = v;
    }

    // 从队列中删除并返回一个double型数据
    public double deQueue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        double value = elements[0];
        // 移动队列中的元素
        System.arraycopy(elements, 1, elements, 0, size - 1);
        size--;
        return value;
    }

    // 返回队列中的第一个元素
    public double getHead() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return elements[0];
    }

    // 返回队列中的最后一个元素
    public double getTail() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return elements[size - 1];
    }

    // 判断队列是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    // 判断队列是否为满
    public boolean isFull() {
        return size == DEFAULT_CAPACITY;
    }

    // 返回队列的大小
    public int getSize() {
        return size;
    }
}