package monitrgw;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import monitrgw.domain.MonitrMarketData;
import monitrgw.domain.MonitrStockDetails;
import monitrgw.domain.async.MonitrServiceAsyncNio;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * @author Miguel Gamboa
 *         created on 07-06-2016
 */
public class MonitrController implements AutoCloseable{

    private static final Template viewStock, viewNews;

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
            return viewNews.apply(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStock(HttpServletRequest req) {
        String symbol = req.getPathInfo().substring(1);
        MonitrStockDetails stock = service
                .getStockDetailsAsync(symbol)
                .apply(symbol);
        String res = null;
        try {
            res = viewStock.apply(stock);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
