import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.stream.Collector;

public class Sorter implements Collector<Order, Map<String, Book>, Map<String, Book>> {

    @Override
    public Supplier<Map<String, Book>> supplier() {
        return () -> new HashMap<String, Book>();
    }

    @Override
    public BiConsumer<Map<String, Book>, Order> accumulator() {
        return (books, order) -> {
            Book book = books.get(order.book);
            if (book == null) {
                book = new Book(order.book);
                books.put(order.book, book);
            }
            book.put(order);
        };
    }

    @Override
    public BinaryOperator<Map<String, Book>> combiner() {
        return (books1, books2) -> {
            for (String name: books2.keySet()) {
                Book book1 = books1.get(name);
                Book book2 = books2.get(name);
                if (book1 == null) {
                    books1.put(name, book2);
                } else {
                    book1.merge(book2);
                }
            }
            return books1;
        };
    }

    @Override 
    public Function<Map<String, Book>, Map<String, Book>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(
            EnumSet.of(
                Characteristics.UNORDERED, 
                Characteristics.CONCURRENT, 
                Characteristics.IDENTITY_FINISH
            )
        );
    }
}
