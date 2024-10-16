package tests;

import main.Queue;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testEnQueueAndDeQueue() {
        Queue queue = new Queue();
        queue.enQueue(1.1);
        queue.enQueue(2.2);
        queue.enQueue(3.3);

        assertEquals(1.1, queue.deQueue(), 0.001);
        assertEquals(2.2, queue.deQueue(), 0.001);
        assertEquals(3.3, queue.deQueue(), 0.001);
    }

    @Test
    public void testGetHeadAndGetTail() {
        Queue queue = new Queue();
        queue.enQueue(1.1);
        queue.enQueue(2.2);
        queue.enQueue(3.3);

        assertEquals(1.1, queue.getHead(), 0.001);
        assertEquals(3.3, queue.getTail(), 0.001);
    }

    @Test
    public void testIsEmptyAndIsFull() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertFalse(queue.isFull());

        for (int i = 0; i < 32; i++) {
            queue.enQueue(i);
        }

        assertFalse(queue.isEmpty());
        assertTrue(queue.isFull());
    }

    @Test
    public void testGetSize() {
        Queue queue = new Queue();
        assertEquals(0, queue.getSize());

        queue.enQueue(1.1);
        queue.enQueue(2.2);

        assertEquals(2, queue.getSize());

        queue.deQueue();
        assertEquals(1, queue.getSize());
    }
}