package monitrgw;

import monitrgw.domain.MonitrStockDetails;
import monitrgw.domain.async.MonitrServiceAsyncNio;

import javax.servlet.http.HttpServletRequest;

import static java.util.stream.Collectors.joining;

/**
 * @author Miguel Gamboa
 *         created on 07-06-2016
 */
public class MonitrController implements AutoCloseable{

    private static final String viewStock = "<html><body><ul>" +
            "<li>Name: %s</li>" +
            "<li>Desc: %s</li>" +
            "<li>Sector: %s</li>" +
            "</ul></body></html>";

    private final MonitrServiceAsyncNio service;

    public String getNews(HttpServletRequest req) {
        return service
                .GetLastNews()
                .map(Object::toString)
                .collect(joining());
    }

    public String getStock(HttpServletRequest req) {
        String symbol = req.getPathInfo().substring(1);
        MonitrStockDetails stock = service
                .getStockDetailsAsync(symbol)
                .apply(symbol);
        String res = String.format(viewStock, stock.getName(), stock.getDescription(), stock.getSector());
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
