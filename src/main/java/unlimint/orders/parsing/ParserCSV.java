package unlimint.orders.parsing;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;
import unlimint.orders.model.Order;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParserCSV implements FileParser {
    public List<Order> parse(String fileName) {
        List<Order> orders = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            int lineCounter = 1;
            List<String[]> lines = reader.readAll();

            for (String[] line: lines) {
                Order order = new Order();
                order.setResult("OK");
                try {
                    order.setId(Integer.parseInt(line[0]));
                } catch (NumberFormatException e) {
                    order.setResult("Invalid data type in order id");
                }
                try {
                    order.setAmount(BigDecimal.valueOf(Double.parseDouble(line[1])));
                } catch (NumberFormatException e) {
                    order.setResult("Invalid data type in amount");
                }
                order.setCurrency(line[2]);
                order.setComment(line[3]);
                order.setFilename(fileName);
                order.setLine(lineCounter++);
                orders.add(order);
            }
        } catch (FileNotFoundException e) {
            System.err.printf("Unable to open file '%s'%n", fileName);
        } catch (IOException e) {
            System.err.printf("Error reading file '%s'%n", fileName);
        } catch (CsvException e) {
            System.err.printf("Error reading file ''%s'%n", fileName);
        }
        return orders;
    }
}
