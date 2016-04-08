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
        return (item1, item2) -> getter.apply(item1).compareTo(getter.apply(item2));
    }
}

interface CmpCompasable<T> extends Comparator<T>{

    // abstract public int compare(T o1, T o2); // Implícitamente herdado de Coparator<T>

    public default <R extends Comparable<R>> CmpCompasable<T> andThen(Function<T, R> getter) {
        return (item1, item2) -> {
            int res = this.compare(item1, item2);
            return res != 0 ? res : getter.apply(item1).compareTo(getter.apply(item2));
        };
    }
    public default CmpCompasable<T> inverted(){
        return (item1,item2)-> compare(item1, item2) * -1;
    }
}