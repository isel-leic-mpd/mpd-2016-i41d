package monitrgw.domain;

import java.util.stream.Stream;

/**
 * @author Miguel Gamboa
 *         created on 20-05-2016
 */
public interface IMonitrService {
    Stream<MonitrMarketData> GetLastNews();
}
