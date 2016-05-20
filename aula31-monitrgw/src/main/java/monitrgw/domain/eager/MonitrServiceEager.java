package monitrgw.domain.eager;

import monitrgw.domain.IMonitrMarketData;
import monitrgw.domain.IMonitrService;
import monitrgw.domain.eager.model.MonitrMarketData;
import monitrgw.webapi.MonitrApi;
import monitrgw.webapi.dto.MonitrMarketDto;

import java.util.stream.Stream;

/**
 * Created by mcarvalho on 28-05-2015.
 */
public class MonitrServiceEager implements IMonitrService{

    public Stream<IMonitrMarketData> GetLastNews(){
        MonitrMarketDto dto = MonitrApi.GetLastNews(); // 1 http get request
        return dto.data
                .stream()
                .map(MonitrMarketData::valueOf); // 1 lazy Http request per item + 1 http request

    }
}
