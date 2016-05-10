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
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
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



        src
                .stream()
                .collect(groupingBy(WeatherInfo::getWeatherDesc, counting())) // Map<String, Integer>
                .entrySet()                                       // Set<Pair<String, List<WeatherInfo>>>
                .forEach(pair -> System.out.println(pair));
        System.out.println("---------------------------------");
        src
                .stream()
                // .collect(groupingBy(WeatherInfo::getWeatherDesc)) // Map<String, List<WeatherInfo>>
                .collect(groupingBy(WeatherInfo::getWeatherDesc, toList())) // Map<String, List<WeatherInfo>>
                .entrySet()                                                 // Set<Pair<String, List<WeatherInfo>>>
                .stream()                                                   // Stream<Pair<String, List<WeatherInfo>>>
                // .map(p -> p.getKey() + ": [" + p.getValue().stream().map(w -> w.getTempC() + "").reduce((prev, w) -> prev + ", " + w).get()+ "]") // Stream<String>
                .map(p -> p.getKey() + ": [" + p.getValue().stream().map(w -> w.getTempC() + "").collect(joining(","))+ "]") // Stream<String>
                .forEach(System.out::println);
        System.out.println("---------------------------------");
        src
                .stream()
                .collect(groupingBy(
                        WeatherInfo::getWeatherDesc,
                        mapping(w -> w.getTempC(), toSet())
                )) // Map<String, List<Integer>>
                .entrySet()
                .stream()
                .forEach(System.out::println);
        System.out.println("---------------------------------");
        src
                .stream()
                .collect(groupingBy(
                        WeatherInfo::getWeatherDesc,
                        averagingInt(WeatherInfo::getTempC)
                )) // Map<String, Integer>
                .entrySet()
                .stream()
                .forEach(p -> System.out.println(p.getKey() + ": temp media = " + p.getValue()));

    }
}
