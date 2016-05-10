package wthr;

import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collector.Characteristics.UNORDERED;
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static wthr.Queries.*;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
public class App {
    public static void main(String[] args) throws ParseException, IOException, URISyntaxException {
        Function<HistoryArgs, List<WeatherInfo>> historyGetter =
                p -> WeatherHttpGetterFromCsv.getHistory(p.name, p.start, p.end);
        historyGetter = new WeatherHistoryCache(historyGetter);
        List<WeatherInfo> src = WeatherHttpGetterFromCsv
                .getRegions("Porto", historyGetter)
                .iterator()
                .next()
                .getHistory(LocalDate.of(2016,4, 1), LocalDate.of(2016,4, 29));



        src
                .stream()
                .collect(groupingBy(WeatherInfo::getWeatherDesc, counting())) // Map<String, Integer>
                .entrySet()                                       // Set<Pair<String, List<WeatherInfo>>>
                .forEach(pair -> System.out.println(pair));
        System.out.println("---------------------------------");
        src
                .stream()
                // .collect(groupingBy(WeatherInfo::getWeatherDesc)) // Map<String, List<WeatherInfo>>
                .collect(groupingBy(WeatherInfo::getWeatherDesc, toList())) // Map<String, List<WeatherInfo>>
                .entrySet()                                                 // Set<Pair<String, List<WeatherInfo>>>
                .stream()                                                   // Stream<Pair<String, List<WeatherInfo>>>
                // .map(p -> p.getKey() + ": [" + p.getValue().stream().map(w -> w.getTempC() + "").reduce((prev, w) -> prev + ", " + w).get()+ "]") // Stream<String>
                .map(p -> p.getKey() + ": [" + p.getValue().stream().map(w -> w.getTempC() + "").collect(joining(","))+ "]") // Stream<String>
                .forEach(System.out::println);
        System.out.println("---------------------------------");
        /* TPC --> Solução Daniel */
        /*
        src
                .stream()               // Stream<WeatherInfo>
                .collect(groupingBy(
                        WeatherInfo::getWeatherDesc,
                        collectingAndThen(
                                toSet(),
                                s -> s
                                    .stream()
                                    .mapToInt(WeatherInfo::getTempC)
                                    .sorted()
                                    .collect(TreeSet::new, TreeSet::add, TreeSet::addAll))
                )) // Map<String, List<Integer>>
                .entrySet()
                .stream()
                .forEach(System.out::println);
        */
        /* TPC ---> Solução Claudia Soares */
        src
                .stream()
                .collect(groupingBy(
                        WeatherInfo::getWeatherDesc,
                        collectingAndThen(
                                mapping(w -> w.getTempC(), toSet()),
                                TreeSet::new
                        )
                ))
                .entrySet()
                .stream()
                .forEach(System.out::println);

        System.out.println("---------------------------------");
        src
                .stream()
                .collect(groupingBy(
                        WeatherInfo::getWeatherDesc,
                        averagingInt(WeatherInfo::getTempC)
                )) // Map<String, Integer>
                .entrySet()
                .stream()
                .forEach(p -> System.out.println(p.getKey() + ": temp media = " + p.getValue()));
        System.out.println("---------------------------------");
        src
                .stream()
                .collect(
                        partitioningBy(
                                w -> w.getWeatherDesc().equals("Sunny"),
                                mapping(WeatherInfo::getTempC, toList()))) // Map<String, Integer>
                .entrySet()
                .stream()
                .forEach(System.out::println);
        System.out.println("---------------------------------");
        src
                .stream()
                .collect(
                        new PartitionCollector<>(
                                w -> w.getWeatherDesc().equals("Sunny"),
                                mapping(WeatherInfo::getTempC, toList()))) // Map<String, Integer>
                .entrySet()
                .stream()
                .forEach(System.out::println);

    }
}

class PartitionCollector<T, RR> implements Collector<T, Map<Boolean, List<T>>, Map<Boolean, RR>> {

    final Predicate<T> pred;
    final Collector<T, ?, RR> col;

    public PartitionCollector(Predicate<T> pred, Collector<T, ?, RR> col) {
        this.pred = pred;
        this.col = col;
    }

    @Override
    public Supplier<Map<Boolean, List<T>>> supplier() {
        return () -> {
            Map<Boolean, List<T>> res = new HashMap<>();
            res.put(true, new ArrayList<>());
            res.put(false, new ArrayList<>());
            return res;
        };
    }

    @Override
    public BiConsumer<Map<Boolean, List<T>>, T> accumulator() {
        return (map, elem) -> map.get(pred.test(elem)).add(elem);
    }

    @Override
    public BinaryOperator<Map<Boolean, List<T>>> combiner() {
        return (m1, m2) -> {
            m1.get(true).addAll(m2.get(true));
            m1.get(false).addAll(m2.get(false));
            return m1;
        };
    }

    @Override
    public Function<Map<Boolean, List<T>>, Map<Boolean, RR>> finisher() {
        return src -> {
            Map<Boolean, RR> res = new HashMap<>();
            res.put(true, src.get(true).stream().collect(col));
            res.put(false, src.get(false).stream().collect(col));
            return res;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Stream.of(UNORDERED).collect(toSet());
    }
}
