package unlimint.orders.concurrency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import unlimint.orders.model.Order;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private BlockingQueue<Order> orders;
    private final Order POISON;

    public Consumer(BlockingQueue<Order> orders, Order POISON) {
        this.orders = orders;
        this.POISON = POISON;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = orders.take();
                if (order == POISON) {
                    break;
                }
                convert(order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    //converts Order to JSON
    public void convert(Order order) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            System.err.printf("Error when converting order with id = %%n", order.getId());
        }
    }
}
