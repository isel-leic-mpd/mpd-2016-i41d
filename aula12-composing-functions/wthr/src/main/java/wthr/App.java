package wthr;

import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
public class App {
    public static void main(String[] args) throws ParseException, IOException, URISyntaxException {
        // List<WeatherInfo> data = WeatherSupplier.supply();
        // data.forEach(weather -> System.out.println(weather)); // T -> void

        // Queries.<WeatherInfo>forEach(data, weather -> System.out.println(weather));

        // Parâmetro de Tipo T é inferido a partir do parametro formal src = data = List<WeatherInfo> => T = WeatherInfo
        // Queries.forEach(data, weather -> System.out.println(weather));

        /*
        List<WeatherInfo> data = WeatherHttpGetterFromCsv.getHistory(
                "Lisbon",
                LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 10));
        Queries.forEach(data, weather -> System.out.println(weather));
        */
        Function<HistoryArgs, List<WeatherInfo>> historyGetter = p -> {
            System.out.println("******** Get from HTTP");
            return WeatherHttpGetterFromCsv.getHistory(p.name, p.start, p.end);
        };
        historyGetter = new WeatherHistoryCache(historyGetter);
        WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,2, 1), LocalDate.of(2016,2, 29))
                .forEach(System.out::println); // <=> .forEach(r -> System.out.println(r));

        WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,2, 10), LocalDate.of(2016,2, 20))
                .forEach(System.out::println); // <=> .forEach(r -> System.out.println(r));

        WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,3, 10), LocalDate.of(2016,3, 20))
                .forEach(System.out::println); // <=> .forEach(r -> System.out.println(r));
    }
}
