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
                .getRegions("Lisbon", p -> WeatherHttpGetterFromCsv.getHistory(p.name, p.start, p.end))
                .get(0)
                .getHistory(
                    LocalDate.of(2016, 2, 1),
                    LocalDate.of(2016, 2, 29)
                );
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