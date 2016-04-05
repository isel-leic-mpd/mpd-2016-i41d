package wthr;

import wthr.model.WeatherInfo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
public class WeatherSupplier {
    private static final String LISBON_HISTORY = "data/lisbon-weather-history.csv";

    public static List<WeatherInfo> supply() throws ParseException, IOException, URISyntaxException {
        Path p = Paths.get(ClassLoader.getSystemResource(LISBON_HISTORY).toURI());
        Iterator<String> lines = Files.readAllLines(p).iterator();
        List<WeatherInfo> res = new ArrayList<>();
        while(lines.next().startsWith("#"));

        while(lines.hasNext()){
            lines.next(); // Skip Not Available or Daily Info
            String line = lines.next();
            WeatherInfo info = WeatherInfo.valueOf(line);
            res.add(info);
        }
        return res;
    }
}
