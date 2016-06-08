package monitrgw;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import monitrgw.domain.MonitrMarketData;
import monitrgw.domain.MonitrStockDetails;
import monitrgw.domain.async.MonitrServiceAsyncNio;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author Miguel Gamboa
 *         created on 07-06-2016
 */
public class MonitrController implements AutoCloseable{

    private static final Template viewStock, viewNews;
    private static final Map<String, String> cacheViewsStocks = new HashMap<>();

    static { // Construtor de classe
        try {
            Handlebars handlebars = new Handlebars();
            viewStock = handlebars.compile("ViewStock");
            viewNews = handlebars.compile("ViewNews");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final MonitrServiceAsyncNio service;

    private String viewsStockBindTo(MonitrStockDetails stock) {
        try {
            return viewStock.apply(stock);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNews(HttpServletRequest req) throws IOException {
        List<MonitrMarketData> model = service.GetLastNews().collect(toList());
        // Iniciar a criação das Views para cada Stock
        // Iterar sobre as noticias, map -> Symbol, distinct, gerar uma View;
        Map<String, String> stockViews = model
                .stream()
                .map(MonitrMarketData::getStockDetails)
                .map(CompletableFuture::join)
                .distinct()
                .collect(toMap(
                    stock -> "/stock/" + stock.getSymbol(),
                    stock -> viewsStockBindTo(stock))
                );
        cacheViewsStocks.putAll(stockViews);
        return viewNews.apply(model);
    }

    public String getStock(HttpServletRequest req) throws IOException {
        String res = cacheViewsStocks.get(req.getRequestURI());
        if(res != null) return res;
        String symbol = req.getPathInfo().substring(1);
        MonitrStockDetails stock = service
                .getStockDetails(symbol)
                .join();
        res = viewStock.apply(stock);
        cacheViewsStocks.put(req.getRequestURI(), res);
        return res;
    }

    public MonitrController(MonitrServiceAsyncNio service) {
        this.service = service;
    }

    @Override
    public void close() throws Exception {
        if(!service.isClosed())
            service.close();
    }
}
