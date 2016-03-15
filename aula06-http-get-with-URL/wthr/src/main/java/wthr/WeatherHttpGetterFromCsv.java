package wthr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel Gamboa
 *         created on 15-03-2016
 */
public class WeatherHttpGetterFromCsv {

    private static final String WEATHER_HOST = "http://api.worldweatheronline.com";
    private static final String WEATHER_PAST =
            "/free/v2/past-weather.ashx?key=%s&q=%s&format=csv&date=%s&enddate=%s&tp=24";
    private static final String WEATHER_SEARCH =
            "/free/v2/search.ashx?key=%s&query=%s&format=tab";
    private static final String WEATHER_TOKEN = "25781444d49842dc5be040ff259c5";

    public static List<WeatherRegion> getRegions(String query) throws IOException, ParseException {
        String uri = WEATHER_HOST + String.format(WEATHER_SEARCH,
                WEATHER_TOKEN,
                query);
        try (InputStream in = new URL(uri).openStream()) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                List<WeatherRegion> res = new ArrayList<>();
                String line;
                while ((line = reader.readLine()).startsWith("#")) ;
                while(line != null){
                    WeatherRegion info = WeatherRegion.valueOf(line);
                    res.add(info);
                    line = reader.readLine();
                }
                return res;
            }
        }
    }
    public static List<WeatherInfo> getHistory(String location, LocalDate start, LocalDate end) throws IOException, ParseException {
        List<WeatherInfo> res = new ArrayList<>();
        String uri = WEATHER_HOST + String.format(WEATHER_PAST,
                WEATHER_TOKEN, location, start, end);
        try (InputStream in = new URL(uri).openStream()) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = reader.readLine()).startsWith("#")) ;
                while((line = reader.readLine()) != null){
                    line = reader.readLine(); // Skip Not Available or Daily Info
                    WeatherInfo info = WeatherInfo.valueOf(line);
                    res.add(info);
                }
                return res;
            }
        }
    }
}
