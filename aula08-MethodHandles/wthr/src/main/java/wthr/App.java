package wthr;

import wthr.model.WeatherRegion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

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
        List<WeatherRegion> data = WeatherHttpGetterFromCsv.getRegions("Porto");
        Queries.forEach(data, r -> System.out.println(r));
    }
}
