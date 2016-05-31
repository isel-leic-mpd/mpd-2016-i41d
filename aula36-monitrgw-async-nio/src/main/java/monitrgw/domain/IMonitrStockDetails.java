package monitrgw.domain;

/**
 * Created by mcarvalho on 26-05-2015.
 */
public interface  IMonitrStockDetails {

    public String getIndustry();

    public String getName();

    public String getSector();

    public String getSymbol();

    public int getStatus();

    public String getDescription();

    public IMonitrStockAnalysisData getAnalysis();
}
