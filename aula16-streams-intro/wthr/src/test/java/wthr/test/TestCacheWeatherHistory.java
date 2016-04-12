package wthr.test;

import org.junit.Assert;
import junit.framework.TestCase;
import wthr.WeatherHistoryCache;
import wthr.WeatherHttpGetterFromCsv;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

/**
 * @author Miguel Gamboa
 *         created on 01-04-2016
 */
public class TestCacheWeatherHistory extends TestCase{

    /*
    class Counter<T> implements Function<T, T> {
        int nr;
        @Override
        public T apply(T t) {
            nr++;
            return t;
        }
    }
    */

    public void testCacheHttpGetterFromCSV() throws ParseException, IOException, URISyntaxException {
        Function<HistoryArgs, List<WeatherInfo>> historyGetter =
                p -> WeatherHttpGetterFromCsv.getHistory(p.name, p.start, p.end);
        int [] httpCounter = {0}, cacheCounter  = {0};

        historyGetter =
                new WeatherHistoryCache(historyGetter.andThen(hist -> {httpCounter[0]++; return hist;}))
                .andThen(hist -> {cacheCounter[0]++; return hist;});

        Assert.assertEquals(0, httpCounter[0]);
        Assert.assertEquals(0, cacheCounter[0]);

        WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,3, 1), LocalDate.of(2016,3, 29))
                .forEach(System.out::println); // <=> .forEach(r -> System.out.println(r));

        Assert.assertEquals(1, httpCounter[0]);
        Assert.assertEquals(1, cacheCounter[0]);

        WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,3, 10), LocalDate.of(2016,3, 20))
                .forEach(System.out::println); // <=> .forEach(r -> System.out.println(r));

        Assert.assertEquals(1, httpCounter[0]);
        Assert.assertEquals(2, cacheCounter[0]);

        WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,4, 1), LocalDate.of(2016,4, 4))
                .forEach(System.out::println); // <=> .forEach(r -> System.out.println(r));

        Assert.assertEquals(2, httpCounter[0]);
        Assert.assertEquals(3, cacheCounter[0]);

    }
}
