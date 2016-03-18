package wthr.test;

import junit.framework.TestCase;
import org.junit.Assert;
import wthr.*;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Miguel Gamboa
 *         created on 08-03-2016
 */
public class TestQueries extends TestCase {
    List<WeatherInfo> res;

    public TestQueries() throws IOException, ParseException {
        /*
        this.res = WeatherHttpGetterFromCsv.getHistory(
                "Lisbon",
                LocalDate.of(2016, 2, 1),
                LocalDate.of(2016, 2, 29));
         */
        res = WeatherHttpGetterFromCsv
                .getRegions("Lisbon")
                .get(0)
                .getHistory(
                    LocalDate.of(2016, 2, 1),
                    LocalDate.of(2016, 2, 29)
                );
    }

    /**
     * 1st Try
     */
    public void testFilterSunnyDays() throws ParseException, IOException, URISyntaxException {

        Assert.assertEquals(13, Queries.filterSunnyDays(res).size());
    }

    /**
     * 2nd Try
     */
    public void testFilterByDesc() throws ParseException, IOException, URISyntaxException {

        Assert.assertEquals(4, Queries.filterByDesc(res, "Light rain shower").size());
    }

    /**
     * 3rd Try
     */
    public void testFilterByDomainObjectWithTempC() throws ParseException, IOException, URISyntaxException {
        WeatherInfo criteria = new WeatherInfo(null, 18, null, 0, 0) {
            @Override
            public boolean equals(Object obj) {
                WeatherInfo other = (WeatherInfo) obj;
                return other.tempC == this.tempC;
            }
        };
        Assert.assertEquals(5, Queries.filterWeather(res, criteria).size());
    }

    public void testFilterByDomainObjectWithFeelsLikeC() throws ParseException, IOException, URISyntaxException {
        WeatherInfo criteria = new WeatherInfo(null, 0, null, 0, 0) {
            @Override
            public boolean equals(Object obj) {
                WeatherInfo other = (WeatherInfo) obj;
                return other.feelsLikeC == 18;
            }
        };
        Assert.assertEquals(5, Queries.filterWeather(res, criteria).size());
    }

    /**
     * 4th Try
     */
    public void testFilterByPredicateWithFeelsLikeC() throws ParseException, IOException, URISyntaxException {

        Predicate<WeatherInfo> criteria = new Predicate<WeatherInfo>() {
            @Override
            public boolean test(WeatherInfo other) {
                return other.feelsLikeC == 18;
            }
        };
        Assert.assertEquals(5, Queries.filterWeather(res, criteria).size());
    }

    /**
     * 5th Try
     */
    public void testFilterByPredicateWithLambda() throws ParseException, IOException, URISyntaxException {

        // WeatherPredicate criteria = other -> other.feelsLikeC == 18;
        // WeatherPredicate criteria = other -> { return other.feelsLikeC== 18; };
        // WeatherPredicate criteria = (other) -> { return other.feelsLikeC== 18; };
        Predicate<WeatherInfo> criteria = (WeatherInfo other) -> {
            return other.feelsLikeC == 18;
        };
        Assert.assertEquals(5, Queries.filterWeather(res, criteria).size());
    }

    public void testFilterByPredicate() throws ParseException, IOException, URISyntaxException {

        Assert.assertEquals(5, Queries.filter(res, other -> other.feelsLikeC == 18).size());
    }

    public void testMaxTempDay() throws ParseException, IOException, URISyntaxException {
        Comparator<WeatherInfo> cmp = (o1, o2) -> o1.tempC - o2.tempC;
        Integer temps =
                measurePerformance("max", () -> {
                    int ts = Collections.max(res, cmp).tempC;
                    return ts; // Boxing para Integer
                });
        Assert.assertEquals(21, temps.intValue());// Unboxing para int
    }

    public void testMaxTempWithMap() throws ParseException, IOException, URISyntaxException {

        Integer[] temps = measurePerformance("map boxing", () -> {
            Integer[] ts = Queries.map(res, w -> w.tempC, Integer.class); // boxing
            Arrays.sort(ts); // Usa Comparable::compareTo
            return ts;
        });
        Assert.assertEquals(21, temps[temps.length - 1].intValue());
        Assert.assertEquals(11, temps[0].intValue());
    }
    public void testMaxTempWithPrimitives() throws ParseException, IOException, URISyntaxException {

        int[] temps = measurePerformance("primitives", () -> {
            int[] ts = Queries.mapToInt(res, w -> w.tempC); // SEM boxing
            Arrays.sort(ts); // Usa < ou >
            return ts;
        });
        Assert.assertEquals(21, temps[temps.length - 1]);
        Assert.assertEquals(11, temps[0]);
    }

    public static <T> T measurePerformance(String label, Supplier<T> action) {
        long fastest = Long.MAX_VALUE;
        T res = null;
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            res = action.get();
            long duration = (System.nanoTime() - start) / 1_000; // micro seconds
            if (duration < fastest) fastest = duration;
        }
        System.out.println( label + " DONE in: " + fastest + " us");
        return res;
    }
}