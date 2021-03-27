package unlimint.orders.parsing;

import unlimint.orders.model.Order;

import java.util.List;

public interface FileParser {
    List<Order> parse(String filename);
}
