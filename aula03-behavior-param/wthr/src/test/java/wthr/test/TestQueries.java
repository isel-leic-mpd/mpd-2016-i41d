package wthr.test;

import junit.framework.TestCase;
import org.junit.Assert;
import wthr.Queries;
import wthr.WeatherInfo;
import wthr.WeatherPredicate;
import wthr.WeatherSupplier;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author Miguel Gamboa
 *         created on 08-03-2016
 */
public class TestQueries extends TestCase {
    /**
     * 1st Try
     */
    public static void testFilterSunnyDays() throws ParseException, IOException, URISyntaxException {
        List<WeatherInfo> res = WeatherSupplier.supply();
        Assert.assertEquals(13, Queries.filterSunnyDays(res).size());
    }
    /**
     * 2nd Try
     */
    public static void testFilterByDesc() throws ParseException, IOException, URISyntaxException {
        List<WeatherInfo> res = WeatherSupplier.supply();
        Assert.assertEquals(4, Queries.filterByDesc(res, "Light rain shower").size());
    }
    /**
     * 3rd Try
     */
    public static void testFilterByDomainObjectWithTempC() throws ParseException, IOException, URISyntaxException {
        List<WeatherInfo> res = WeatherSupplier.supply();
        WeatherInfo criteria = new WeatherInfo(null, 18, null, 0, 0){
           @Override
            public boolean equals(Object obj) {
                WeatherInfo other = (WeatherInfo) obj;
                return other.tempC == this.tempC;
            }
        };
        Assert.assertEquals(4, Queries.filterWeather(res, criteria).size());
    }

    public static void testFilterByDomainObjectWithFeelsLikeC() throws ParseException, IOException, URISyntaxException {
        List<WeatherInfo> res = WeatherSupplier.supply();
        WeatherInfo criteria = new WeatherInfo(null, 0, null, 0, 0){
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
    public static void testFilterByPredicateWithFeelsLikeC() throws ParseException, IOException, URISyntaxException {
        List<WeatherInfo> res = WeatherSupplier.supply();
        WeatherPredicate criteria = new WeatherPredicate(){
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
    public static void testFilterByPredicateWithLambda() throws ParseException, IOException, URISyntaxException {
        List<WeatherInfo> res = WeatherSupplier.supply();
        WeatherPredicate criteria = other -> other.feelsLikeC == 18;
        Assert.assertEquals(5, Queries.filterWeather(res, criteria).size());
    }
}
