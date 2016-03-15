package wthr;

/**
 * @author Miguel Gamboa
 *         created on 11-03-2016
 */
public class WeatherRegion {
    public final String name;
    public final int population;

    public WeatherRegion(String name, int population) {
        this.name = name;
        this.population = population;
    }

    @Override
    public String toString() {
        return "WeatherRegion{" +
                "name='" + name + '\'' +
                ", population=" + population +
                '}';
    }

    public static WeatherRegion valueOf(String line) {
        String[] data = line.split("\t");
        return new WeatherRegion(data[2], Integer.parseInt(data[5]));
    }
}
