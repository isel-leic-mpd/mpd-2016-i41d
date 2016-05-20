package monitrgw.domain.eager.model;

import monitrgw.domain.IMonitrMarketData;
import monitrgw.domain.IMonitrStockDetails;
import monitrgw.webapi.MonitrApi;
import monitrgw.webapi.dto.MonitrMarketDtoData;
import monitrgw.webapi.dto.MonitrStockDetailsDto;

/**
 * Created by mcarvalho on 28-05-2015.
 */
public class MonitrMarketData implements IMonitrMarketData{
    private final String market;
    private final String title;
    private final String stockSymbol;
    private final String link;
    private final long timeInMilis;
    private final String domain;
    private final IMonitrStockDetails stockDetails;

    public MonitrMarketData(String market, String title, String stockSymbol, String link, long timeInMilis, String domain) {
        this.market = market;
        this.title = title;
        this.stockSymbol = stockSymbol;
        this.link = link;
        this.timeInMilis = timeInMilis;
        this.domain = domain;

        // MonitrStockDetailsDto  ---> MonitrStockDetails
        //
        MonitrStockDetailsDto dto = MonitrApi.GetStockDetails(stockSymbol);
        stockDetails = new MonitrStockDetails(
                dto.industry,
                dto.name,
                dto.sector,
                dto.symbol,
                dto.status,
                dto.description,
                dto.alias,
                dto.competitors);

    }

    public static MonitrMarketData valueOf(MonitrMarketDtoData dto){
        return new MonitrMarketData(
                dto.market,
                dto.title,
                dto.symbol,
                dto.link,
                dto.timeInMilis,
                dto.domain);
    }

    public String getMarket() {
        return market;
    }

    public String getTitle() {
        return title;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getLink() {
        return link;
    }

    public long getTimeInMilis() {
        return timeInMilis;
    }

    public String getDomain() {
        return domain;
    }

    public IMonitrStockDetails getStockDetails() {
        return stockDetails;
    }

    @Override
    public String toString() {
        return "MonitrMarketData{" +
                "market='" + market + '\'' +
                ", title='" + title + '\'' +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", link='" + link + '\'' +
                ", timeInMilis=" + timeInMilis +
                ", domain='" + domain + '\'' +
                ", stockDetails=" + stockDetails +
                '}';
    }
}
