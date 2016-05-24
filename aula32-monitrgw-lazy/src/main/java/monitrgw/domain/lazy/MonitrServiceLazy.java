package monitrgw.domain.lazy;

import monitrgw.domain.IMonitrMarketData;
import monitrgw.domain.IMonitrService;
import monitrgw.domain.IMonitrStockAnalysisData;
import monitrgw.domain.IMonitrStockDetails;
import monitrgw.domain.MonitrMarketData;
import monitrgw.domain.MonitrStockAnalysisData;
import monitrgw.domain.MonitrStockDetails;
import monitrgw.webapi.MonitrApi;
import monitrgw.webapi.dto.MonitrMarketDto;
import monitrgw.webapi.dto.MonitrMarketDtoData;
import monitrgw.webapi.dto.MonitrStockAnalysisDto;
import monitrgw.webapi.dto.MonitrStockAnalysisDtoData;
import monitrgw.webapi.dto.MonitrStockDetailsDto;

import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mcarvalho on 28-05-2015.
 */
public class MonitrServiceLazy implements IMonitrService{

    public Stream<IMonitrMarketData> GetLastNews(){
        MonitrMarketDto dto = MonitrApi.GetLastNews(); // 1 http get request
        return dto
                .data      // List<MonitrMarketDtoData>
                .stream()  // Stream<MonitrMarketDtoData>
                .map(MonitrServiceLazy::dtoToDomain)  // Stream<IMonitrMarketData>
                .collect(Collectors.toList())
                .stream();
    }

    private static IMonitrMarketData dtoToDomain(MonitrMarketDtoData dto) {
        return new MonitrMarketData(
                dto.market,
                dto.title,
                dto.symbol,
                dto.link,
                dto.timeInMilis,
                dto.domain,
                cache(MonitrServiceLazy::getStockDetails) // 1 http get request
        );
    }

    private static IMonitrStockDetails getStockDetails(String symbol) {
        MonitrStockDetailsDto dto = MonitrApi.GetStockDetails(symbol);
        return new MonitrStockDetails(
                dto.industry,
                dto.name,
                dto.sector,
                dto.symbol,
                dto.status,
                dto.description,
                dto.alias,
                dto.competitors,
                cache(s -> getStockAnalysis(s, dto.name)));
    }
    private static <T, R> Function<T, R> cache(Function<T, R> init){
        final Object[] c = {null};
        return arg -> {
            c[0] = c[0] != null ? c[0] : init.apply(arg);
            return (R) c[0];
        };
    }

    private static IMonitrStockAnalysisData getStockAnalysis(String s, String name) {
        MonitrStockAnalysisDtoData a = MonitrApi.GetStockAnalysis(s);
        return new MonitrStockAnalysisData(
                s,
                name,
                a.mentions,
                a.totalSentiment,
                a.averageSentiment,
                a.positive,
                a.negative
        );
    }
}
