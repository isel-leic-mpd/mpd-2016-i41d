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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static wthr.Queries.*;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
public class App {
    public static void main(String[] args) throws ParseException, IOException, URISyntaxException {
        Function<HistoryArgs, List<WeatherInfo>> historyGetter =
                p -> WeatherHttpGetterFromCsv.getHistory(p.name, p.start, p.end);
        historyGetter = new WeatherHistoryCache(historyGetter);
        List<WeatherInfo> src = WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,4, 1), LocalDate.of(2016,4, 29));


        Stream<String> descs2 = src.stream()
                .filter(w -> w.getTempC() >= 10 )
                .map(w -> w.getWeatherDesc())
                .distinct()
                .limit(3);



        src
                .stream()
                .collect(groupingBy(WeatherInfo::getWeatherDesc)) // Map<String, List<WeatherInfo>>
                .entrySet()                                       // Set<Pair<String, List<WeatherInfo>>>
                .forEach(pair -> System.out.println(
                        pair.getKey() + ": " +
                                pair.getValue().stream().map(WeatherInfo::getTempC).collect(Collectors.toList())) );

        src
                .stream()
                .collect(groupingBy(WeatherInfo::getWeatherDesc, counting()))
                .entrySet()
                .forEach(pair -> System.out.println( pair.getKey() + ": " + pair.getValue()));


    }
}
