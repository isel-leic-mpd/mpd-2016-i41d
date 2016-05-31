import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Miguel Gamboa
 *         created on 31-05-2016
 */
public class App {

    public static String httpGet(String path){
        try (InputStream in = new URL(path).openStream()) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String content = reader.lines().collect(Collectors.joining());
                return content;
            }
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<String> httpGetAsync(String path){
        CompletableFuture<String> promise = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            promise.complete(httpGet(path));
        });
        return promise;

        /* <=>
        return CompletableFuture.supplyAsync(() -> {
            return httpGet(path);
        });
        */
    }


    static final String URI_MONITR_DATA = "http://api.monitr.com/api/v1/market/news?apikey=1e3f8640-f754-11e3-97e9-179fff8a3cc5";
    static final String URI_MONITR_APPLE = "http://api.monitr.com/api/v2/symbol?apikey=1e3f8640-f754-11e3-97e9-179fff8a3cc5&symbol=AAPL";
    static final String URI_MONITR_APPLE_ANALYSIS = "http://api.monitr.com/api/v2/symbol/mentions?apikey=1e3f8640-f754-11e3-97e9-179fff8a3cc5&startDay=0&endDay=1&symbol=AAPL";

    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("############ Sync: Pedido e espera resposta HTTP");
        measure(() -> httpGet(URI_MONITR_DATA).length());
        measure(() -> httpGet(URI_MONITR_APPLE).length());
        measure(() -> httpGet(URI_MONITR_APPLE_ANALYSIS).length());

        System.out.println("############ ASync: Pedido...");
        CompletableFuture<String> r1 = measure(() -> httpGetAsync(URI_MONITR_DATA));
        CompletableFuture<String> r2 = measure(() -> httpGetAsync(URI_MONITR_APPLE));
        CompletableFuture<String> r3 = measure(() -> httpGetAsync(URI_MONITR_APPLE_ANALYSIS));

        // System.out.println("############ Doing stuff...");
        // Thread.currentThread().sleep(2000);

        System.out.println("############ ASync: ... espera resposta");
        CompletableFuture<Void> v1 = r1                                        // CompletableFuture<String>
                .thenApply(String::length)        // CompletableFuture<Integer>
                .thenAccept(System.out::println); // CompletableFuture<void>
        CompletableFuture<Void> v2 = r2.thenApply(String::length).thenAccept(System.out::println);
        CompletableFuture<Void> v3 = r3.thenApply(String::length).thenAccept(System.out::println);

        measure(() -> v3.join());
        measure(() -> v1.join());
        measure(() -> v2.join());



        System.out.println("############ ASync NIO: Pedido...");

        try(AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient()) {
            CompletableFuture<Response> p1 = measure(() -> asyncHttpClient.prepareGet(URI_MONITR_DATA).execute().toCompletableFuture());
            CompletableFuture<Response> p2 = measure(() -> asyncHttpClient.prepareGet(URI_MONITR_APPLE).execute().toCompletableFuture());
            CompletableFuture<Response> p3 = measure(() -> asyncHttpClient.prepareGet(URI_MONITR_APPLE_ANALYSIS).execute().toCompletableFuture());

            System.out.println("############ ASync NIO: ... espera resposta");
            measure(() -> p1.join().getResponseBody().length());
            measure(() -> p2.join().getResponseBody().length());
            measure(() -> p3.join().getResponseBody().length());
            asyncHttpClient.close();
        }
    }


    public static <T> T measure(Supplier<T> action) {
        long start = System.nanoTime();
        T res = action.get();
        long duration = (System.nanoTime() - start) / 1_000; // micro seconds
        System.out.println( "[" + duration + " us] " + res);
        return res;
    }
}
