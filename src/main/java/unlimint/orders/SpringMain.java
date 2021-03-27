package unlimint.orders;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import unlimint.orders.concurrency.Consumer;
import unlimint.orders.concurrency.Producer;
import unlimint.orders.model.Order;
import unlimint.orders.parsing.ParserCSV;
import unlimint.orders.parsing.ParserJSON;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SpringMain {
    private static final String CSV = "CSV";
    private static final String JSON = "JSON";
    private static final Order POISON = new Order();

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring_app.xml")) {
            BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

            for (String fileName : args) {
                String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase();
                switch (ext) {
                    case CSV:
                        new Thread(new Producer(fileName,
                                orderQueue,
                                appCtx.getBean(ParserCSV.class))).start();
                        break;
                    case JSON:
                        new Thread(new Producer(fileName,
                                orderQueue,
                                appCtx.getBean(ParserJSON.class))).start();
                        break;
                    default:
                        System.err.printf("Wrong format  file = %s%n", fileName);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                while (true) {
                    try {
                        for (int i = 0; i < args.length; i++) {
                            orderQueue.put(POISON);
                        }
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (int i = 0; i < args.length; i++) {
                new Thread(new Consumer(orderQueue, POISON)).start();
            }
        }
    }
}
