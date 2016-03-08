package wthr;

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
        List<WeatherInfo> data = WeatherSupplier.supply();
        data.forEach(weather -> System.out.println(weather)); // T -> void

    }
}
