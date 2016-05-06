package wthr.test;

import junit.framework.TestCase;
import org.junit.Assert;
import wthr.model.WeatherInfo;
import wthr.WeatherSupplier;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
public class TestParseWeatherFile extends TestCase{

    public void testParseFile() throws ParseException, IOException, URISyntaxException {
        List<WeatherInfo> res = WeatherSupplier.supply();
        Assert.assertEquals(29, res.size());
    }
}
