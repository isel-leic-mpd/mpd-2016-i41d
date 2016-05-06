package wthr;

import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Miguel Gamboa
 *         created on 01-04-2016
 */
public class WeatherHistoryCache implements Function<HistoryArgs, List<WeatherInfo>> {
    private final List<WeatherInfo> cache;
    private final Function<HistoryArgs, List<WeatherInfo>> dataSrc;

    public WeatherHistoryCache(Function<HistoryArgs, List<WeatherInfo>> dataSrc) {
        this.cache = new ArrayList<>();
        this.dataSrc = dataSrc;
    }

    @Override
    public List<WeatherInfo> apply(HistoryArgs historyArgs) {
        LocalDate start, end;

        // Verificar cache miss e definir [start, end]
        for(start = historyArgs.start; start.isBefore(historyArgs.end); start = start.plusDays(1)) {
            final LocalDate date = start;
            Optional<WeatherInfo> item = Queries.find(cache, w -> w.date.equals(date));
            if(!item.isPresent()) break;
        }
        for(end = historyArgs.end; end.isAfter(start); end = end.minusDays(1)) {
            final LocalDate date = end;
            Optional<WeatherInfo> item = Queries.find(cache, w -> w.date.equals(date));
            if(!item.isPresent()) break;
        }

        // Se start < end ===> get data from dataSrc ===> actualizar Cache
        if(start.isBefore(end)) {
            List<WeatherInfo> toAdd = dataSrc.apply(new HistoryArgs(historyArgs.name, start, end));
            toAdd = Queries.filter(toAdd, w -> !cache.contains(w)); // toAdd.removeAll(cache);
            cache.addAll(toAdd);
            cache.sort((w1, w2) -> w1.date.compareTo(w2.date));
        }

        // Return SubList
        int startIdx = Queries.indexOf(cache, w -> w.date.equals(historyArgs.start));
        int endIdx = (int) (startIdx + ChronoUnit.DAYS.between(historyArgs.start, historyArgs.end));
        return cache.subList(startIdx, endIdx);
    }
}
