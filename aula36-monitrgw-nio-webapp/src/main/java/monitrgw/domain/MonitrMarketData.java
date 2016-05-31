package monitrgw.domain;

import monitrgw.webapi.MonitrApi;
import monitrgw.webapi.dto.MonitrMarketDtoData;
import monitrgw.webapi.dto.MonitrStockDetailsDto;

import java.util.function.Function;

/**
 * Created by mcarvalho on 28-05-2015.
 */
public class MonitrMarketData{
    private final String market;
    private final String title;
    private final String stockSymbol;
    private final String link;
    private final long timeInMilis;
    private final String domain;
    private final Function<String, MonitrStockDetails> stockDetails;

    public MonitrMarketData(
            String market,
            String title,
            String stockSymbol,
            String link,
            long timeInMilis,
            String domain,
            Function<String, MonitrStockDetails> stockDetails) {
        this.market = market;
        this.title = title;
        this.stockSymbol = stockSymbol;
        this.link = link;
        this.timeInMilis = timeInMilis;
        this.domain = domain;
        this.stockDetails = stockDetails;
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

    public MonitrStockDetails getStockDetails() {
        return stockDetails.apply(stockSymbol);
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
