package monitrgw.domain.async;

import monitrgw.domain.IMonitrService;
import monitrgw.domain.MonitrMarketData;
import monitrgw.domain.MonitrStockAnalysisData;
import monitrgw.domain.MonitrStockDetails;
import monitrgw.webapi.MonitrApi;
import monitrgw.webapi.dto.MonitrMarketDto;
import monitrgw.webapi.dto.MonitrMarketDtoData;
import monitrgw.webapi.dto.MonitrStockAnalysisDtoData;
import monitrgw.webapi.dto.MonitrStockDetailsDto;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mcarvalho on 28-05-2015.
 */
public class MonitrServiceAsyncNio implements IMonitrService, AutoCloseable{

    private final MonitrApi api = new MonitrApi();

    public Stream<MonitrMarketData> GetLastNews(){
        CompletableFuture<MonitrMarketDto> dto = api.GetLastNews(); // 1 http get request
        return dto
                .join()
                .data      // List<MonitrMarketDtoData>
                .stream()  // Stream<MonitrMarketDtoData>
                .map(this::dtoToDomain);  // Stream<IMonitrMarketData>
    }

    private MonitrMarketData dtoToDomain(MonitrMarketDtoData dto) {
        return new MonitrMarketData(
                dto.market,
                dto.title,
                dto.symbol,
                dto.link,
                dto.timeInMilis,
                dto.domain,
                getStockDetailsAsync(dto.symbol)
        );
    }

    private Function<String, MonitrStockDetails> getStockDetailsAsync(String symbol) {
        CompletableFuture<MonitrStockDetails> stock = getStockDetails(symbol); // 1 http get request
        return (s) -> stock.join(); // Waiting for response
    }


    private CompletableFuture<MonitrStockDetails> getStockDetails(String symbol) {
        CompletableFuture<MonitrStockDetailsDto> promise = api.GetStockDetails(symbol);
        return promise.thenApply(dto ->
                new MonitrStockDetails(
                dto.industry,
                dto.name,
                dto.sector,
                dto.symbol,
                dto.status,
                dto.description,
                dto.alias,
                dto.competitors,
                getStockAnalysis(symbol, dto.name)));
    }

    private Function<String, MonitrStockAnalysisData> getStockAnalysis(String s, String name) {
        CompletableFuture<MonitrStockAnalysisData> res = api
                .GetStockAnalysis(s)
                .thenApply(dto -> new MonitrStockAnalysisData(
                    s,
                    name,
                    dto.mentions,
                    dto.totalSentiment,
                    dto.averageSentiment,
                    dto.positive,
                    dto.negative));

        return (symbol) -> res.join();
    }

    @Override
    public void close() throws Exception {
        if(! api.isClosed()) api.close();
    }


}
