package wthr;

import wthr.model.WeatherInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * @author Miguel Gamboa
 *         created on 08-03-2016
 */
public class Queries {

    public static <T> List<T> filter(List<T> src, Predicate<T> criteria)  {
        List<T> res = new ArrayList<>();
        for (T item : src) {
            if (criteria.test(item)) {
                res.add(item);
            }
        }
        return res;
    }

    public static <T, R> List<R> map(Collection<T> src, Function<T, R> f)  {
        List<R> res = new ArrayList<>();
        int i = 0;
        for (T item : src) {
            res.add(f.apply(item));
        }
        return res;
    }

    public static <T> List<T> distinct(List<T> src)  {
        List<T> res = new ArrayList<>();
        for (T item : src) {
            if (!find(res, obj -> obj.equals(item)).isPresent()) {
                res.add(item);
            }
        }
        return res;
    }
    public static <T> List<T> limit(List<T> src, int max)  {
        return src.subList(0, max); // !!!!! the result is IMMUTABLE !!!!
    }

    public static <T> Optional<T> find(List<T> src, Predicate<T> criteria)  {
        for (T item : src) {
            if (criteria.test(item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public static <T> int indexOf(List<T> src, Predicate<T> criteria)  {
        for (int i = 0; i < src.size(); i++) {
            if (criteria.test(src.get(i))) {
                return i;
            }
        }
        return -1;
    }

}
