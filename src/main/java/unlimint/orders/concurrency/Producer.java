package unlimint.orders.concurrency;

import unlimint.orders.model.Order;
import unlimint.orders.parsing.FileParser;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private String fileName;
    private BlockingQueue<Order> ordersQueue;
    private FileParser parser;

    public Producer(String fileName, BlockingQueue<Order> ordersQueue, FileParser parser) {
        this.fileName = fileName;
        this.ordersQueue = ordersQueue;
        this.parser = parser;
    }

    @Override
    public void run() {
        try {
            for (Order order: parser.parse(fileName)) {
                ordersQueue.put(order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
