package queries;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Miguel Gamboa
 *         created on 14-04-2016
 */
public interface Queryable<T> extends Spliterator<T>{

    public static <T> Queryable<T> of(List<T> src) {
        int [] idx = {0};
        return action -> { // Implementacao de tryAdvance
            if(idx[0] < src.size()){
                action.accept(src.get(idx[0]++));
                return true;
            }
            else return false;
        };
    }

    public default <R> Queryable<R> map(Function<T, R> mapper) {
        return action ->  // Implementacao de tryAdvance
            // <=> this.tryAdvance(item ->
            tryAdvance(item ->
                action.accept(mapper.apply(item)));
    }

    public default Queryable<T> filter(Predicate<T> p) {
        return action -> {// Implementacao de tryAdvance
                boolean[] found = {false};
                while(!found[0] && tryAdvance(item -> {
                    if (p.test(item)){
                        found[0] = true;
                        action.accept(item);
                    }
                })){}
                return found[0];
        };
    }

    public default Queryable<T> limit(int max) {
        int [] count = {0};
        return action -> {// Implementacao de tryAdvance
            if(count[0]++ < max)
                return tryAdvance(action);
            else
                return false;
        };
    }

    public default void forEach(Consumer<T> action) {
        while(tryAdvance(action)){}
    }

    public default Queryable<T> distinct() {
        HashSet<T> selected = new HashSet<>();
        return action -> { // Implementing tryAdvance
            boolean[] found = {false};
            while(!found[0] && this.tryAdvance( item -> {
                if(selected.add(item)) {
                    action.accept(item);
                    found[0] = true;
                }
            }));
            return found[0];
        };
    }

    @Override
    public default Spliterator<T> trySplit() {
        return null; // null means this spliterator cannot be split
    }

    @Override
    public default long estimateSize() {
        throw new NotImplementedException();
    }

    @Override
    public default int characteristics() {
        throw new NotImplementedException();
    }
}