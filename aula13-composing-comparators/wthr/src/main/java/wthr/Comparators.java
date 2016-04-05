package wthr;

import wthr.model.WeatherInfo;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author Miguel Gamboa
 *         created on 05-04-2016
 */
public class Comparators {

    public static <T, R extends Comparable<R>> CmpCompasable<T> comparing(Function<T, R> getter) {
        Comparator<T> c = (item1, item2) -> getter.apply(item1).compareTo(getter.apply(item2));
        return new CmpCompasable<>(c);
    }
    public static class CmpCompasable<T> implements Comparator<T>{
        final Comparator<T> c;

        public CmpCompasable(Comparator<T> c) {
            this.c = c;
        }
        @Override
        public int compare(T o1, T o2) {
            return c.compare(o1, o2);
        }

        public <R extends Comparable<R>> Comparator<T> andThen(Function<T, R> getter) {
            return (item1, item2) -> {
                int res = this.compare(item1, item2);
                return res != 0 ? res : getter.apply(item1).compareTo(getter.apply(item2));
            };
        }
    }
}

