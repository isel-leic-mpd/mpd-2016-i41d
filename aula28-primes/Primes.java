import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class Primes {

    /*==================================================
      ==================================================*/
    public static boolean isPrime(int n) {
        int root = (int) Math.sqrt(n);
        return IntStream
            .rangeClosed(2, root)
            .noneMatch(div -> n%div == 0);
    }
    public static List<Integer> primes(int max) {
        return IntStream
            .range(2, max)
            .filter(Primes::isPrime)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /*==================================================
      ==================================================*/
    
    public static boolean isPrimeOpt(List<Integer> primes, int n) {
        int root = (int) Math.sqrt(n);
        return takeWhile(primes, root)
            .stream()
            .noneMatch(div -> n%div == 0);
    }
    
    public static List<Integer> takeWhile(List<Integer> src, int max) {
        int i;
        for(i = 0; i < src.size() && src.get(i) <= max; i++) {}
        return src.subList(0, i);
    }
    
    public static List<Integer> primesOpt(int max) {
        ArrayList<Integer> res = new ArrayList<>();
        return IntStream
            .range(2, max)
            .parallel()
            .filter(n -> Primes.isPrimeOpt(res, n))
            .collect(() -> res, ArrayList::add, (l1, l2) -> {});
    }
    
    public static void main(String [] args) {
        
        primes(100).forEach(n -> System.out.print(n + " ") );
        System.out.println();
        primesOpt(100).forEach(n -> System.out.print(n + " ") );
        System.out.println();
        
        measurePerformance(() -> primesOpt(1_000_000));
        measurePerformance(() -> primes(1_000_000));
    }
    
    public static <T> T measurePerformance(Supplier<T> action) {
      long fastest = Long.MAX_VALUE;
      T res = null;
      for (int i = 0; i < 5; i++) {
          long start = System.nanoTime();
          res = action.get();
          long duration = (System.nanoTime() - start) / 1_000_000; // mili seconds
          System.out.println( "> " + duration + " ms");
          if (duration < fastest) fastest = duration;
      }
      System.out.println( "DONE in: " + fastest + " ms");
      return res;
  }
}
