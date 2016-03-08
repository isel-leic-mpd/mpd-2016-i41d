package wthr;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
    public static List<WeatherInfo> filterWeather(List<WeatherInfo> src, WeatherPredicate criteria)  {
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
    public static List<WeatherInfo> filter(List<WeatherInfo> src, Predicate<WeatherInfo> criteria)  {
        List<WeatherInfo> res = new ArrayList<>();
        for (WeatherInfo item : src) {
            if (criteria.test(item)) {
                res.add(item);
            }
        }
        return res;
    }

}
