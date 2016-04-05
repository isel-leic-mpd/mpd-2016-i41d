package wthr;

import util.HttpGetter;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    public static List<WeatherRegion> getRegions(
            String query,
            Function<HistoryArgs, List<WeatherInfo>> historyGetter
    ) throws IOException, ParseException {
        String uri = WEATHER_HOST + String.format(WEATHER_SEARCH,
                WEATHER_TOKEN,
                query);
        return HttpGetter.httpGet(uri, (reader, line) -> Parsers.parseWeatherRegion(reader, line, historyGetter));
    }

    public static List<WeatherInfo> getHistory(String location, LocalDate start, LocalDate end) {
        String uri = WEATHER_HOST + String.format(WEATHER_PAST,
                WEATHER_TOKEN, location, start, end);
        try {
            return HttpGetter.httpGet(uri, Parsers::parseWeatherInfo);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
