package wthr;

import wthr.model.WeatherInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * @author Miguel Gamboa
 *         created on 08-03-2016
 */
public class Queries {

    /**
     * 1 st try
     */
    public static List<WeatherInfo> filterSunnyDays(List<WeatherInfo> src) {
        List<WeatherInfo> res = new ArrayList<>();
        for (WeatherInfo item : src) {
            if(item.weatherDesc.equals("Sunny"))
                res.add(item);
        }
        return res;
    }

    /**
     * 2 nd try
     */
    public static List<WeatherInfo> filterByDesc(List<WeatherInfo> src, String desc) {
        List<WeatherInfo> res = new ArrayList<>();
        for (WeatherInfo item : src) {
            if(item.weatherDesc.equals(desc))
                res.add(item);
        }
        return res;
    }
    /**
     * 3 rd try
     */
    public static List<WeatherInfo> filterWeather(List<WeatherInfo> src, WeatherInfo criteria)  {
        List<WeatherInfo> res = new ArrayList<>();
        for (WeatherInfo item : src) {
            if (criteria.equals(item)) {
                res.add(item);
            }
        }
        return res;
    }

    /**
     * 4 th try
     */
    public static List<WeatherInfo> filterWeather(List<WeatherInfo> src, Predicate<WeatherInfo> criteria)  {
        List<WeatherInfo> res = new ArrayList<>();
        for (WeatherInfo item : src) {
            if (criteria.test(item)) {
                res.add(item);
            }
        }
        return res;
    }
    /**
     * 5 th try
     */
    public static <T> List<T> filter(List<T> src, Predicate<T> criteria)  {
        List<T> res = new ArrayList<>();
        for (T item : src) {
            if (criteria.test(item)) {
                res.add(item);
            }
        }
        return res;
    }

    /**
     * forEach
     */
    public static <T> void forEach(Iterable<T> src, Consumer<T> c)  {
        for (T item : src) {
            c.accept(item);
        }
    }
    /**
     * map
     */
    public static <T, R> R[] map(Collection<T> src, Function<T, R> f, Class<R> klass)  {
        R[] res = (R[]) Array.newInstance(klass, src.size());
        int i = 0;
        for (T item : src) {
            res[i++] = f.apply(item);
        }
        return res;
    }
    /**
     * mapToInt
     */
    public static <T> int[] mapToInt(Collection<T> src, ToIntFunction<T> f)  {
        int[] res = new int[src.size()];
        int i = 0;
        for (T item : src) {
            res[i++] = f.applyAsInt(item);
        }
        return res;
    }
}
