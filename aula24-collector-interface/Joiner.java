import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class Joiner {
    public static void main(String [] args) {
    
        IntStream nrs = IntStream.range(65, 130);
        Stream<String> src = nrs.mapToObj(nr -> ((char) nr) + "");
        System.out.println(join1(src, ", "));
        
        
    }
    
    public static <T> String join1(Stream<T> src, String separator) {
        BinaryOperator<String> acc = (prev, curr) -> prev + separator + curr;
        return src
            // .parallel()
            .map(Object::toString) // T -> String
            .reduce(
                "", 
                acc, 
                acc);
            
    }
    public static <T> String join2(Stream<T> src, String separator) {
        Collector<String, String, String> col = new Collector<String, String, String>() {
        
        }
    }
}
