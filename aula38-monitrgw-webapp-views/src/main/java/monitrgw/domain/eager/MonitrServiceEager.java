package monitrgw.domain.eager;

import monitrgw.domain.IMonitrService;
import monitrgw.domain.MonitrMarketData;
import monitrgw.domain.MonitrStockAnalysisData;
import monitrgw.domain.MonitrStockDetails;
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
public class MonitrServiceEager implements IMonitrService, AutoCloseable{

    private final MonitrApi api = new MonitrApi();

    public Stream<MonitrMarketData> GetLastNews(){
        MonitrMarketDto dto = api.GetLastNews().join(); // 1 http get request
        return dto
                .data      // List<MonitrMarketDtoData>
                .stream()  // Stream<MonitrMarketDtoData>
                .map(this::dtoToDomain)  // Stream<IMonitrMarketData>
                .collect(Collectors.toList())
                .stream();
    }

    private MonitrMarketData dtoToDomain(MonitrMarketDtoData dto) {
        MonitrStockDetails stockDetails = getStockDetails(dto.symbol); // 1 http get request
        return new MonitrMarketData(
                dto.market,
                dto.title,
                dto.symbol,
                dto.link,
                dto.timeInMilis,
                dto.domain,
                (symbol) -> stockDetails
        );
    }

    private MonitrStockDetails getStockDetails(String symbol) {
        MonitrStockDetailsDto dto = api.GetStockDetails(symbol).join();
        MonitrStockAnalysisDtoData a = api.GetStockAnalysis(dto.symbol).join();

        MonitrStockAnalysisData analysis = new MonitrStockAnalysisData(
                dto.symbol,
                dto.name,
                a.mentions,
                a.totalSentiment,
                a.averageSentiment,
                a.positive,
                a.negative
        );
        return new MonitrStockDetails(
                dto.industry,
                dto.name,
                dto.sector,
                dto.symbol,
                dto.status,
                dto.description,
                dto.alias,
                dto.competitors,
                s -> analysis);
    }

    @Override
    public void close() throws Exception {
        if(! api.isClosed()) api.close();
    }

}
