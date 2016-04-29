import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class Collectors {
    public static void main(String [] args) {
    
        IntStream nrs = IntStream.range(0, 1024);
        
        
        // Thread-safe -- Não há partilha de estado
        // cada sub-stream usa objectos ArrayList distintos
        // CORRECTA -- cumpre os requisitos de reduce()
        nrs
            .parallel()
            .mapToObj(nr -> new ArrayList<>(Arrays.asList(nr)))
            .reduce((p, c) -> {p.addAll(c); return p;})
            .ifPresent(l -> System.out.println(l.size()));
        
        // Non thread-safe - Há partilha de estado
        // O mesmo objecto ArrayList (1º argumento do reduce) é partilhado
        // pelos vários sub-streams
        // INCORRECTA -- não segue os requisitos do reduce
        nrs = IntStream.range(0, 1024);
        List<Integer> mem = nrs
            // .parallel() => pode provocar execepção de acessos concorrentes ao mesmo Indice
            .mapToObj(Integer::new)
            .reduce(
                new ArrayList<>(), 
                (l, nr) -> { l.add(nr); return l; },
                (l1, l2) -> {l1.addAll(l2); return l1;});
        System.out.println(mem.size());
        
        // Thread-safe
        nrs = IntStream.range(0, 1024);
        mem = nrs
            .parallel()
            .mapToObj(Integer::new)
            // .collect(() -> new ArrayList<>(), (l, nr) -> l.add(nr), (l1,l2) -> l1.addAll(l2));
            .collect(ArrayList::new, List::add, List::addAll);
        System.out.println(mem.size());
    }
}
