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
public class Queryable<T> implements Spliterator<T>{

    final Function<Consumer<? super T>, Boolean> srcTryAdvance;

    public Queryable(Function<Consumer<? super T>, Boolean> srcTryAdvance) {
        this.srcTryAdvance = srcTryAdvance;
    }

    public static <T> Queryable<T> of(List<T> src) {
        int [] idx = {0};
        return new Queryable<>(action -> { // Implementacao de tryAdvance
            if(idx[0] < src.size()){
                action.accept(src.get(idx[0]++));
                return true;
            }
            else return false;
        });
    }

    public <R> Queryable<R> map(Function<T, R> mapper) {
        return new Queryable<>(action ->  // Implementacao de tryAdvance
            srcTryAdvance.apply(item ->
                action.accept(mapper.apply(item)))

        );
    }

    public Queryable<T> filter(Predicate<T> p) {
        throw new NotImplementedException();
    }

    public Queryable<T> limit(int max) {
        int [] count = {0};
        return new Queryable<>(action -> {// Implementacao de tryAdvance
            if(count[0]++ < max)
                return srcTryAdvance.apply(action);
            else
                return false;
        });
    }

    public void forEach(Consumer<T> action) {
        while(srcTryAdvance.apply(action)){ }
    }

    public Queryable<T> distinct() {
        HashSet<T> selected = new HashSet<>();
        return new Queryable<>(action -> { // Implementing tryAdvance
            boolean[] found = {false};
            while(!found[0] && this.tryAdvance( item -> {
                if(selected.add(item)) {
                    action.accept(item);
                    found[0] = true;
                }
            }));
            return found[0];
        });
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return srcTryAdvance.apply(action);
    }

    @Override
    public Spliterator<T> trySplit() {
        return null; // null means this spliterator cannot be split
    }

    @Override
    public long estimateSize() {
        throw new NotImplementedException();
    }

    @Override
    public int characteristics() {
        throw new NotImplementedException();
    }
}