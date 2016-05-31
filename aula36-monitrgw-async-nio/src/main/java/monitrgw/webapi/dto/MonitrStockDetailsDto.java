package monitrgw.webapi.dto;

import java.util.List;

/**
 * Created by mcarvalho on 26-05-2015.
 */
public class MonitrStockDetailsDto {
    public final String industry;
    public final String name;
    public final String sector;
    public final String symbol;
    public final int status;
    public final String description;
    public final List<String> alias;
    public final List<String> competitors;

    public MonitrStockDetailsDto(List<String> alias, List<String> competitors, String description, String industry, String name, String sector, String symbol, int status) {
        this.alias = alias;
        this.competitors = competitors;
        this.description = description;
        this.industry = industry;
        this.name = name;
        this.sector = sector;
        this.symbol = symbol;
        this.status = status;
    }

    @Override
    public String toString() {
        return "MonitrStockDetailsDto{" +
                "industry='" + industry + '\'' +
                ", name='" + name + '\'' +
                ", sector='" + sector + '\'' +
                ", symbol='" + symbol + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", alias=" + alias +
                ", competitors=" + competitors +
                '}';
    }
}
