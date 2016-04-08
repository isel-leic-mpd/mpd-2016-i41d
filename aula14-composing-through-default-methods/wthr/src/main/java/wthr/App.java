package wthr;

import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
public class App {
    public static void main(String[] args) throws ParseException, IOException, URISyntaxException {
        Function<HistoryArgs, List<WeatherInfo>> historyGetter = p -> {
            System.out.println("******** Get from HTTP");
            return WeatherHttpGetterFromCsv.getHistory(p.name, p.start, p.end);
        };
        historyGetter = new WeatherHistoryCache(historyGetter);
        List<WeatherInfo> src = WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,3, 1), LocalDate.of(2016,3, 29));

        src.sort( Comparators
                .comparing(WeatherInfo::getWeatherDesc)
                .andThen(WeatherInfo::getPrecipMM)
                .andThen(WeatherInfo::getTempC)
                .inverted()
                .inverted()
        );
        src.forEach(System.out::println);

    }
}
