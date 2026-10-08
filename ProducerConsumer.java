class SharedBuffer {
    private int data;
    private boolean available = false;

    // Producer method
    public synchronized void produce(int value) throws InterruptedException {
        while (available) {
            wait();
        }
        data = value;
        System.out.println("Produced: " + data);
        available = true;
        notify();
    }

    // Consumer method
    public synchronized int consume() throws InterruptedException {
        while (!available) {
            wait();
        }
        System.out.println("Consumed: " + data);
        available = false;
        notify();
        return data;
    }
}

class Producer extends Thread {
    private SharedBuffer buffer;

    Producer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                buffer.produce(i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer extends Thread {
    private SharedBuffer buffer;

    Consumer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                buffer.consume();
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer();

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();
    }
}