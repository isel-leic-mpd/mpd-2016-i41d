import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class StreamRange {

    
    public static boolean isPrime1(int n) {
        for(int i = 2; i < n; i++)
            if(n%i == 0)
                return false;
        return true;
    }
    
    
    public static boolean isPrime2(int n) {
        return LongStream
            .range(2, n)
            .filter(div -> n%div == 0)
            .findAny()
            .isPresent() == false;
    }
    
    public static boolean isPrime3(int n) {
        return LongStream
            .range(2, n)
            .noneMatch(div -> n%div == 0);
    }

    public static void main(String [] args) {
        
        IntStream.range(3,9).forEach(n -> System.out.print(n + " ") );
        IntStream.rangeClosed(3,9).forEach(n -> System.out.print(n + " ") );
    }
}
