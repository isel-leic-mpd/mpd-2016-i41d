package monitrgw.domain.lazy;

import monitrgw.domain.IMonitrMarketData;
import monitrgw.domain.IMonitrService;

import java.util.stream.Stream;

/**
 * Created by mcarvalho on 28-05-2015.
 */
public class MonitrServiceLazy implements IMonitrService{
    public Stream<IMonitrMarketData> GetLastNews(){
        return null;
    }
}
