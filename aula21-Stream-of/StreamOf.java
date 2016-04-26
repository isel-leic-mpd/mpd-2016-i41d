import java.util.*;
import java.util.function.*;

@FunctionalInterface
interface Nonspliterator<T> extends Spliterator<T>{

    @Override
    public default Spliterator<T> trySplit() {
        return null; // null means this spliterator cannot be split
    }

    @Override
    public default long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public default int characteristics() {
        return 0;
    }
}

@FunctionalInterface
interface Queryable<T> extends Nonspliterator<T> {
    public static <T> Queryable<T> of(T...src) {
        final int[] idx = {0};
        return action -> {
            if(idx[0] < src.length) {
                action.accept(src[idx[0]++]);
                return true;
            }
            return false;
        };
    }
    public static <T> Queryable<T> of(Iterable<T> src) {
        final Iterator<T> iter = src.iterator();
        return action -> {
            if(iter.hasNext()) {
                action.accept(iter.next());
                return true;
            }
            return false;
        };
    }
    
    public static <T> Queryable<T> empty() {
        return action -> false;
    }
    
    public static <T> Queryable<T> generate(Supplier<T> s) {
        return action -> { action.accept(s.get()); return true;};
    }

    public default Queryable<T> limit(int max) {
        final int [] count = {0};
        return action -> count[0]++ < max ? tryAdvance(action) : false;
    }
    
    public default void forEach(Consumer<T> action) {
        while(this.tryAdvance(action)) {}
    }
}

public class StreamOf {

    public static void main(String [] args) {
        Queryable.of(Arrays.asList("abc", "ola", "isel"))
            .forEach(System.out::println);
        
        Queryable.of("abc", "ola", "isel").forEach(System.out::println);
        
        Queryable.empty().forEach(System.out::println);
        
        Queryable
            .generate(() -> Math.random())
            .limit(5)
            .forEach(System.out::println);
    }
}
