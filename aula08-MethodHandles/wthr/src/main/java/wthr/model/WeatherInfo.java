package wthr.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
public class WeatherInfo {
    private static final SimpleDateFormat dateParser =
            new SimpleDateFormat("yyyy-MM-dd");

    public final Date date; // index 0
    public final int tempC; // index 2
    public final String weatherDesc; // index 10
    public final double  precipMM; // index 11;
    public final int feelsLikeC; // index 24;

    public WeatherInfo(Date date, int tempC, String weatherDesc, double precipMM, int feelsLikeC) {
        this.date = date;
        this.tempC = tempC;
        this.weatherDesc = weatherDesc;
        this.precipMM = precipMM;
        this.feelsLikeC = feelsLikeC;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" + "date=" + date + ", tempC=" + tempC + ", weatherDesc=" + weatherDesc + ", precipMM=" + precipMM + ", feelsLikeC=" + feelsLikeC + '}';
    }


    /**
     * Hourly information follows below the day according to the format of
     * /past-weather resource of the World Weather Online API
     */
    public static WeatherInfo valueOf(String input) throws ParseException {
        String[] data = input.split(",");
        return new WeatherInfo(
                dateParser.parse(data[0]),
                Integer.parseInt(data[2]),
                data[10],
                Double.parseDouble(data[11]),
                Integer.parseInt(data[24]));
    }
}
