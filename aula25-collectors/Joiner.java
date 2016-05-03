import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class Joiner {
    public static void main(String [] args) {
    
        IntStream nrs = IntStream.range(65, 130);
        Stream<String> src = nrs.mapToObj(nr -> ((char) nr) + "");
        // System.out.println(join1(src, ", "));
        // System.out.println(join2(src, ", ")); // ERRADO ->demonstra que String é imutavel
        System.out.println(join3(src, ", "));
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
    
    /*
     * Does not work because string is immutable.
     */ 
    public static <T> String join2(Stream<T> src, String separator) {
        Collector<T, String, String> col = new Collector<T, String, String>() {
            public Supplier<String> supplier(){  return () -> "Init: ";  }
            public BiConsumer<String, T> accumulator() { return (prev, curr) -> prev = prev + separator + curr; }
            public BinaryOperator<String> combiner() { return (prev, curr) -> prev + separator + curr; }
            public Function<String, String> finisher() {  return Function.identity();  }
            public Set<Characteristics> characteristics(){
                HashSet<Characteristics> res = new HashSet<>();
                res.add(Characteristics.CONCURRENT);
                res.add(Characteristics.IDENTITY_FINISH);
                return res;
           }
        };
        return src.parallel().collect(col);
    }
    
    public static <T> String join3(Stream<T> src, String separator) {
        Collector<T, StringBuilder, String> col = new Collector<T, StringBuilder, String>() {
            public Supplier<StringBuilder> supplier(){  return StringBuilder::new;  }
            public BiConsumer<StringBuilder, T> accumulator() { return (builder, curr) -> builder.append(curr).append(separator); }
            public BinaryOperator<StringBuilder> combiner() { return (b1, b2) -> b1.append(b2); }
            public Function<StringBuilder, String> finisher() {  return b -> b.substring(0, b.length() - separator.length()); }
            public Set<Characteristics> characteristics(){
                HashSet<Characteristics> res = new HashSet<>();
                res.add(Characteristics.CONCURRENT);
                return res;
           }
        };
        return src.parallel().collect(col);
    }
}
