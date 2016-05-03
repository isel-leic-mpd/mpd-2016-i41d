package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Miguel Gamboa
 *         created on 18-03-2016
 */
public class HttpGetter {
    public static <T> List<T> httpGet(String uri, BiFunction<BufferedReader, String, List<T>> conv) throws IOException {
        try (InputStream in = new URL(uri).openStream()) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = reader.readLine()).startsWith("#")) ;
                return conv.apply(reader, line);
            }
        }
    }
}
