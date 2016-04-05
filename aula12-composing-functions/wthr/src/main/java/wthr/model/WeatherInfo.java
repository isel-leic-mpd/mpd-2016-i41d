package wthr.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
public class WeatherInfo {
    public final LocalDate date; // index 0
    public final int tempC; // index 2
    public final String weatherDesc; // index 10
    public final double  precipMM; // index 11;
    public final int feelsLikeC; // index 24;

    public WeatherInfo(LocalDate date, int tempC, String weatherDesc, double precipMM, int feelsLikeC) {
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
                LocalDate.parse(data[0]),
                Integer.parseInt(data[2]),
                data[10],
                Double.parseDouble(data[11]),
                Integer.parseInt(data[24]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherInfo that = (WeatherInfo) o;

        if (tempC != that.tempC) return false;
        if (Double.compare(that.precipMM, precipMM) != 0) return false;
        if (feelsLikeC != that.feelsLikeC) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return weatherDesc != null ? weatherDesc.equals(that.weatherDesc) : that.weatherDesc == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = date != null ? date.hashCode() : 0;
        result = 31 * result + tempC;
        result = 31 * result + (weatherDesc != null ? weatherDesc.hashCode() : 0);
        temp = Double.doubleToLongBits(precipMM);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + feelsLikeC;
        return result;
    }
}
