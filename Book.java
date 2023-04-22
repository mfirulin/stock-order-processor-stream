import java.util.Objects;
import java.util.Map;
import java.util.TreeMap;

public class Book {
    
    private static class Pair {
        int buy;
        int sell;
    }

    private final String name;
    private Map<Float, Pair> pairs = new TreeMap<>();

    public Book(String name) {
        this.name = name;
    }

    public void merge(Book other) {
        for (Float key: other.pairs.keySet()) {
            Pair otherPair = other.pairs.get(key);
            Pair thisPair = this.pairs.get(key);
            if (thisPair == null) {
                pairs.put(key, otherPair);
            } else {
                thisPair.buy += otherPair.buy;
                thisPair.sell += otherPair.sell;
            }
        }
    }

    public void put(Order order) {
        Pair pair = pairs.get(order.price);
        if (pair == null) {
            pair = new Pair();
            pairs.put(order.price, pair);
        }

        if (order.operation == Order.Operation.BUY) {
            pair.buy += order.volume;
        } else {
            pair.sell += order.volume;
        }
        
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Book[name=");
        builder.append(name);
        builder.append(' ');
        builder.append("size=");
        builder.append(pairs.size());

        // builder.append('\n');
        // for (Map.Entry<Float, Pair> entry : pairs.entrySet()) {
        //     builder.append(
        //         String.format("%6d %7.1f %6d\n", 
        //         entry.getValue().buy, entry.getKey(), entry.getValue().sell)
        //     );
        // }

        builder.append(']');
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;

        Book otherBook = (Book)other;
        return name == otherBook.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
