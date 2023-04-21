import java.util.Map;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        final String PATH = "stock_orders.xml";

        String path = (args.length > 0) ? args[0] : PATH;

        long start = System.nanoTime();

        Stream<String> lines = Reader.read(path);
        Stream<Order> orders = Parser.parse(lines);
        Map<String, Book> books = orders.collect(new Sorter());
        System.out.println("Books: " + books.size());
        books.values().forEach(System.out::println);

        System.out.println("Time taken: " + (System.nanoTime() - start) / 1_000_000 + " ms");
    }
}