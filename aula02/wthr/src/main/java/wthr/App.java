package wthr;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
public class App {
    public static void main(String[] args) throws ParseException, IOException, URISyntaxException {
        WeatherSupplier.supply().forEach(System.out::println);

    }
}
