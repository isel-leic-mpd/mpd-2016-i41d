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
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
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
                .getHistory(LocalDate.of(2016,3, 1), LocalDate.of(2016,3, 29));

        /*
        src = filter(src, w -> w.getTempC() >= 10);
        List<String> descs = map(src, WeatherInfo::getWeatherDesc);
        descs = distinct(descs);
        descs = limit(descs, 3);
        descs.forEach(System.out::println);
        */

        List<String> descs = limit(
                distinct(
                        map(
                            filter(
                                    src,
                                    w -> {
                                        System.out.println("Filtering"); return w.getTempC() >= 10; }),
                            w -> {System.out.println("Mapping"); return w.getWeatherDesc();})),
                3);

        descs.forEach(System.out::println);

        Stream<String> descs2 = src.stream()
                .filter(w -> { System.out.println("Filtering"); return w.getTempC() >= 10; })
                .map(w -> {System.out.println("Mapping"); return w.getWeatherDesc();})
                .distinct()
                .limit(3);

        descs2.forEach(System.out::println);

    }
}
