package monitrgw.domain.lazy;

import monitrgw.domain.IMonitrService;
import monitrgw.domain.MonitrMarketData;
import monitrgw.domain.MonitrStockAnalysisData;
import monitrgw.domain.MonitrStockDetails;
import monitrgw.util.Cache;
import monitrgw.webapi.MonitrApi;
import monitrgw.webapi.dto.MonitrMarketDto;
import monitrgw.webapi.dto.MonitrMarketDtoData;
import monitrgw.webapi.dto.MonitrStockAnalysisDtoData;
import monitrgw.webapi.dto.MonitrStockDetailsDto;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mcarvalho on 28-05-2015.
 */
public class MonitrServiceLazy implements IMonitrService{

    public Stream<MonitrMarketData> GetLastNews(){
        MonitrMarketDto dto = MonitrApi.GetLastNews(); // 1 http get request
        return dto
                .data      // List<MonitrMarketDtoData>
                .stream()  // Stream<MonitrMarketDtoData>
                .map(MonitrServiceLazy::dtoToDomain)  // Stream<IMonitrMarketData>
                .collect(Collectors.toList())
                .stream();
    }

    private static MonitrMarketData dtoToDomain(MonitrMarketDtoData dto) {
        return new MonitrMarketData(
                dto.market,
                dto.title,
                dto.symbol,
                dto.link,
                dto.timeInMilis,
                dto.domain,
                Cache.cache(MonitrServiceLazy::getStockDetails) // 1 http get request
        );
    }

    private static MonitrStockDetails getStockDetails(String symbol) {
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
                Cache.cache(s -> getStockAnalysis(s, dto.name)));
    }

    private static MonitrStockAnalysisData getStockAnalysis(String s, String name) {
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
