package wthr.model;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

/**
 * @author Miguel Gamboa
 *         created on 11-03-2016
 */
public class WeatherRegion {
    public final String name;
    public final int population;
    private final Function<HistoryArgs, List<WeatherInfo>> historyGetter;

    public WeatherRegion(String name, int population, Function<HistoryArgs, List<WeatherInfo>> historyGetter) {
        this.name = name;
        this.population = population;
        this.historyGetter = historyGetter;
    }

    @Override
    public String toString() {
        return "WeatherRegion{" +
                "name='" + name + '\'' +
                ", population=" + population +
                '}';
    }

    public List<WeatherInfo> getHistory(LocalDate start, LocalDate end) {
            return historyGetter.apply( new HistoryArgs(
                    this.name,
                    start,
                    end));
    }

    public static WeatherRegion valueOf(String line, Function<HistoryArgs, List<WeatherInfo>> historyGetter) {
        String[] data = line.split("\t");
        return new WeatherRegion(data[2], Integer.parseInt(data[5]), historyGetter);
    }

}
