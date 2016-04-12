package wthr;

import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Miguel Gamboa
 *         created on 01-04-2016
 */
public class Parsers {
    public static List<WeatherRegion> parseWeatherRegion(
            BufferedReader reader,
            String last,
            Function<HistoryArgs, List<WeatherInfo>> historyGetter
    ) {
        List<WeatherRegion> res = new ArrayList<WeatherRegion>();
        while(last != null){
            res.add(WeatherRegion.valueOf(last, historyGetter));
            try {
                last = reader.readLine();
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return res;
    }

    public static List<WeatherInfo> parseWeatherInfo(BufferedReader reader, String last) {
        List<WeatherInfo> res = new ArrayList<>();
        try {
            while((last = reader.readLine()) != null) {
                last = reader.readLine(); // Skip Not Available or Daily Info
                WeatherInfo info = WeatherInfo.valueOf(last);
                res.add(info);
            }
        } catch (IOException | ParseException e) {
            throw new Error(e);
        }
        return res;
    }

}
