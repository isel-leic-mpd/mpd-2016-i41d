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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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

    public String getNews(HttpServletRequest req) {
        List<MonitrMarketData> model = service.GetLastNews().collect(toList());
        try {
            // Iniciar a criação das Views para cada Stock
            // Iterar sobre as noticias, map -> Symbol, distinct, gerar uma View;
            model.forEach(
                    n -> {
                        try {
                            cacheViewsStocks.put("stock/" + n.getStockSymbol(), viewStock.apply(n.getStockDetails()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            return viewNews.apply(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStock(HttpServletRequest req) {
        String res = cacheViewsStocks.get(req.getRequestURI());
        if(res != null) return res;
        String symbol = req.getPathInfo().substring(1);
        MonitrStockDetails stock = service
                .getStockDetailsAsync(symbol)
                .apply(symbol);
        try {
            res = viewStock.apply(stock);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
