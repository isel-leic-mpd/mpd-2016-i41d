package queries;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Miguel Gamboa
 *         created on 14-04-2016
 */
public class App {
    public static void wordsLengthAvg1(List<String> src){
        int count = 0;
        double sum = 0;
        for (String word: src) {
            count++;
            sum += word.length();
        }
    }

    public static void wordsLengthAvg2(List<String> src){
        int [] count = {0};
        double [] sum = {0};
        src
                .stream()
                .map(String::length)
                .forEach(w -> {count[0]++; sum[0] += w;});
        System.out.printf("Numero total de palavras = %d\n", count[0]);
        System.out.printf("Tamanho medio das palavras = %.2f\n", (sum[0]/count[0]));
    }


    static class Averager {
        int count; double sum;
        void inc(int length){ count++; sum += length; }
        double avg() {return sum/count; }
    }

    /*
     * !!!!!!!!! NO THREAD SAFE
     */
    public static void wordsLengthAvg3(List<String> src){
        Averager avg = new Averager();
        src
                .stream()
                .parallel()
                .map(String::length)
                .forEach(avg::inc);
        System.out.printf("Numero total de palavras = %d\n", avg.count);
        System.out.printf("Tamanho medio das palavras = %.2f\n", avg.avg());
    }

    static class ImmutableAverager {
        final int count; final double sum;
        public ImmutableAverager(int count, double sum) {
            this.count = count;
            this.sum = sum;
        }
        ImmutableAverager inc(int length){ return new ImmutableAverager(count + 1, sum + length); }
        ImmutableAverager combine(ImmutableAverager other){ return new ImmutableAverager(count + other.count, sum + other.sum); }
        double avg() {return sum/count; }
    }

    public static void wordsLengthAvg4(List<String> src){
        ImmutableAverager avg = src
                .stream()
                .parallel()
                .map(String::length)
                .reduce(new ImmutableAverager(0,0), (acc, item) -> acc.inc(item), (a1, a2) -> a1.combine(a2));
        System.out.printf("Numero total de palavras = %d\n", avg.count);
        System.out.printf("Tamanho medio das palavras = %.2f\n", avg.avg());
    }

    public static void wordsLengthAvg5(List<String> src){
        ImmutableAverager avg = src
                .stream()
                .parallel()
                .map(w -> new ImmutableAverager(1, w.length()))
                .reduce(new ImmutableAverager(0,0), ImmutableAverager::combine);
        System.out.printf("Numero total de palavras = %d\n", avg.count);
        System.out.printf("Tamanho medio das palavras = %.2f\n", avg.avg());
    }

    public static void wordsLengthAvg6(List<String> src){
        src
                .stream()
                .parallel()
                .map(w -> new ImmutableAverager(1, w.length()))
                .reduce(ImmutableAverager::combine)
                // .get(); // Se stream vaxio -> Optional vazio -> Excepção
                .ifPresent(avg -> {
                    System.out.printf("Numero total de palavras = %d\n", avg.count);
                    System.out.printf("Tamanho medio das palavras = %.2f\n", avg.avg());
                });
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        Path p = Paths.get(ClassLoader.getSystemResource("src.txt").toURI());
        List<String> src = Arrays.asList(
                Files.readAllLines(p).get(0).split(" "));

        wordsLengthAvg4(src);
        wordsLengthAvg5(src);
        wordsLengthAvg6(src);
        wordsLengthAvg6(Arrays.asList());

        // src.stream().parallel().forEach(System.out::println);
    }
}
