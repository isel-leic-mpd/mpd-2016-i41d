import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class Collectors {
    public static void main(String [] args) {
    
        IntStream nrs = IntStream.range(0, 1024);
        List<Integer> mem = nrs
            .parallel()
            .mapToObj(Integer::new)
            // .collect(new ToListCollector<Integer>());
            .collect(java.util.stream.Collectors.toList());
        System.out.println("size = "  + mem.size() + " type = " + mem.getClass());
    }
}


class ToListCollector<T> implements Collector<T, List<T>, List<T>> {
    public Supplier<List<T>> supplier(){ 
        return ArrayList::new; 
    }
    public BiConsumer<List<T>, T> accumulator() { 
        return List::add; 
    }
    public BinaryOperator<List<T>> combiner() { 
        return (prev, curr) -> {prev.addAll(curr); return prev;}; 
    }
    public Function<List<T>, List<T>> finisher() { 
        return Function.identity(); // <=>= item -> item
    }
    public Set<Characteristics> characteristics(){
        HashSet<Characteristics> res = new HashSet<>();
        res.add(Characteristics.CONCURRENT);
        res.add(Characteristics.IDENTITY_FINISH);
        return res;
   }
}
