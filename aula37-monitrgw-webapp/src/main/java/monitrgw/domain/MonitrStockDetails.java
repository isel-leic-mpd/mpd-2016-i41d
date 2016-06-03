package monitrgw.domain;

import monitrgw.domain.IMonitrStockAnalysisData;
import monitrgw.domain.IMonitrStockDetails;
import monitrgw.webapi.MonitrApi;
import monitrgw.webapi.dto.MonitrStockAnalysisDtoData;

import java.util.List;
import java.util.function.Function;

/**
 * Created by mcarvalho on 28-05-2015.
 */
public class MonitrStockDetails implements IMonitrStockDetails{
    private final String industry;
    private final String name;
    private final String sector;
    private final String symbol;
    private final int status;
    private final String description;
    private final List<String> alias;
    private final List<String> competitors;
    private final Function<String, IMonitrStockAnalysisData> analysis;

    public MonitrStockDetails(
            String industry,
            String name,
            String sector,
            String symbol,
            int status,
            String description,
            List<String> alias,
            List<String> competitors,
            Function<String, IMonitrStockAnalysisData> analysis) {
        this.industry = industry;
        this.name = name;
        this.sector = sector;
        this.symbol = symbol;
        this.status = status;
        this.description = description;
        this.alias = alias;
        this.competitors = competitors;
        this.analysis = analysis;
    }

    @Override
    public String getIndustry() {
        return industry;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSector() {
        return sector;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public List<String> getAlias() {
        return alias;
    }

    public List<String> getCompetitors() {
        return competitors;
    }

    @Override
    public IMonitrStockAnalysisData getAnalysis() {
        return analysis.apply(symbol);
    }
}
