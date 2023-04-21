import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Parser {

    public static Stream<Order> parse(Stream<String> lines) {
        Map<Boolean, List<Order>> orders = lines
            .parallel()
            .map(Parser::parseOrder)
            .filter(Objects::nonNull)
            .collect(Collectors.partitioningBy(Order::isNoneOperationOrder));

        // For some reason solution with
        // partitioningBy(Order::isNoneOperationOrder, toSet()))
        // works slower than default collector toList.
        // So move manually a list to a set for quick removal.
        Set<Order> addOrders = new HashSet<>(orders.get(false));
        addOrders.removeAll(orders.get(true));

        return addOrders.stream();
    }

    private static Order parseOrder(String line) {
        Order result = null;
        if (line.startsWith("\t<A")) {
            result = parseAddOrder(line);
        } else if (line.startsWith("\t<D")) {
            result = parseDeleteOrder(line);
        }
        return result;
    }

    // <AddOrder book="stock-32" operation="SELL" price="76.53" volume="207" orderId="29" />
    private static Order parseAddOrder(String line) {
        String[] values = new String[5];

        for (int i = 0, start = 0; i < values.length; i++) {
            start = line.indexOf('\"', start);
            start++;
            int end = line.indexOf('\"', start);
            values[i] = line.substring(start, end);
            start = end + 1;
        }

        String book = values[0];
        Order.Operation operation = Order.Operation.valueOf(values[1]);
        float price = Float.parseFloat(values[2]);
        int volume = Integer.parseInt(values[3]);
        int id = Integer.parseInt(values[4]);

        return new Order(id, book, operation, price, volume);
    }

    // <DeleteOrder orderId="16" />
    private static Order parseDeleteOrder(String line) {
        int start = line.indexOf('\"');
        int end = line.indexOf('\"', start + 1);
        
        return new Order(Integer.parseInt(line.substring(start + 1, end)));
    }
}
