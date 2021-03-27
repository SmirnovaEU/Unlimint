package unlimint.orders.parsing;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import unlimint.orders.model.Order;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParserJSON implements FileParser {
    public List<Order> parse(String fileName) {
        List<Order> orders = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            int lineCounter = 1;
            for (String line : lines) {
                JSONObject obj = new JSONObject(line);
                Order order = new Order();
                order.setResult("OK");
                try {
                    order.setId((Integer) obj.get("orderId"));
                } catch (JSONException e) {
                    order.setResult(e.getMessage());
                } catch (ClassCastException e) {
                    order.setResult("Invalid data type in orderId");
                }
                try {
                    order.setAmount((BigDecimal) obj.get("amount"));
                } catch (JSONException e) {
                    order.setResult(e.getMessage());
                } catch (ClassCastException e) {
                    order.setResult("Invalid data type in amount");
                }
                try {
                    order.setCurrency((String) obj.get("currency"));
                } catch (JSONException e) {
                    order.setResult(e.getMessage());
                }
                try {
                    order.setComment((String) obj.get("comment"));
                } catch (JSONException e) {
                    order.setResult(e.getMessage());
                }
                order.setFilename(fileName);
                order.setLine(lineCounter++);
                orders.add(order);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Unable to open file '" + fileName + "'" + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file '" + fileName + "'" + e.getMessage());
        }

        return orders;
    }
}
