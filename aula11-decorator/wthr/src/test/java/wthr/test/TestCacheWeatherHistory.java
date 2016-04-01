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


    class CounterFuncCall<T, R> implements Function<T, R> {
        private final Function<T, R> handler;
        int nr;

        CounterFuncCall(Function<T, R> handler) {
            this.handler = handler;
        }

        @Override
        public R apply(T t) {
            nr++;
            return handler.apply(t);
        }
    }

    public void testParseFile() throws ParseException, IOException, URISyntaxException {
        Function<HistoryArgs, List<WeatherInfo>> historyGetter =
                p -> WeatherHttpGetterFromCsv.getHistory(p.name, p.start, p.end);
        CounterFuncCall<HistoryArgs, List<WeatherInfo>> httpCounter = new CounterFuncCall<>(historyGetter);

        historyGetter = new WeatherHistoryCache(httpCounter);
        CounterFuncCall<HistoryArgs, List<WeatherInfo>> cacheCounter = new CounterFuncCall<>(historyGetter);
        historyGetter = cacheCounter;

        Assert.assertEquals(0, httpCounter.nr);
        Assert.assertEquals(0, cacheCounter.nr);

        WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,2, 1), LocalDate.of(2016,2, 29))
                .forEach(System.out::println); // <=> .forEach(r -> System.out.println(r));

        Assert.assertEquals(1, httpCounter.nr);
        Assert.assertEquals(1, cacheCounter.nr);

        WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,2, 10), LocalDate.of(2016,2, 20))
                .forEach(System.out::println); // <=> .forEach(r -> System.out.println(r));

        Assert.assertEquals(1, httpCounter.nr);
        Assert.assertEquals(2, cacheCounter.nr);

        WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,3, 10), LocalDate.of(2016,3, 20))
                .forEach(System.out::println); // <=> .forEach(r -> System.out.println(r));

        Assert.assertEquals(2, httpCounter.nr);
        Assert.assertEquals(3, cacheCounter.nr);

    }
}
