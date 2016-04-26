import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class StreamIter {

    public static Stream<Integer> pares(int init) {
        return Stream.iterate(init, prev -> prev + 2);
    }
        
    public static IntStream fibonacci1() {
        int[] node = {0, 1};
        return IntStream
          .generate(() -> {
                int val = node[0]; 
                node[0] = node[1]; 
                node[1] = node[1] + val;
                return val;}
          );
          
    }
    
    static class Node {
        final int prev, curr; 
        public Node(int p, int c) { prev = p; curr = c; }
    }

    public static IntStream fibonacci2() {
        return Stream
          .iterate(new Node(0, 1), prev -> new Node(prev.curr, prev.prev + prev.curr))
          .mapToInt(n -> n.prev);
    }
    
    
    public static void main(String [] args) {
        
        // pares(4).limit(5).forEach(System.out::println);
            
        fibonacci1().limit(20).forEach(n -> System.out.print(n + " ") );
        System.out.println();
        fibonacci2().limit(20).forEach(n -> System.out.print(n + " ") );
    }
}
