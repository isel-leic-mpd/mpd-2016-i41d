import java.io.*;
import java.util.*;
import java.util.function.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

public class  App {
    
    public static List<String> wordsFrom(String path) throws Exception {
        List<String> doc = Files.readAllLines(new File(path).toPath(), StandardCharsets.UTF_8);
        List<String> words = new ArrayList<>();
        for(String line : doc) {
            words.addAll(Arrays.asList(line.split(" ")));
        }
        return words;
    }

    public static void main(String [] args) throws Exception {
        List<String> words = wordsFrom("src.txt");
        System.out.println("nr of words = " + words.size());
        System.out.println("Sum words length = " + measurePerformance(() -> internalSum(words)));
        System.out.println("Sum words length = " + measurePerformance(() -> externalSum(words)));
    }
    
    public static int externalSum(List<String> data) {
        int sum = 0;
        for(String line : data) // Cada iteração cham o next() e hashNext()
            sum += line.length();
        return sum;
    }
    
    public static int internalSum(List<String> data) {
        int [] sum = {0};
        data.forEach(l -> sum[0] += l.length()); // Cada iteração chama a lambda (Consumer<T>)
        return sum[0];
    }
    
    public static <T> T measurePerformance(Supplier<T> action) {
        long fastest = Long.MAX_VALUE;
        T res = null;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            res = action.get();
            long duration = (System.nanoTime() - start); // / 1_000; // micro seconds
            if (duration < fastest) fastest = duration;
        }
        System.out.println( "DONE in: " + fastest + " ns");
        return res;
    }
}