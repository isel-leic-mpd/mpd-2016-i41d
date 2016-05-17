import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Function;

interface Queryable<T>{
  boolean tryAdvance(Consumer<? super T> action);

  public static <T> Queryable<T> of(Supplier<T> sup) {
    return action -> { action.accept(sup.get()); return true; };
  }
  
  public default <R> Queryable<R> map(Function<T, R> mapper) {
     return action -> tryAdvance(item -> action.accept(mapper.apply(item)));
  }

  public default void forEach(Consumer<T> action) {
    while(this.tryAdvance(action)){};
  }  
  
  public default Queryable<T> limit(long maxSize) { 
    int[] idx = {0};
    return action -> idx[0]++ >= maxSize? false : tryAdvance(action); 
  }
}

public class App {
    public static void main(String[] args) {
         Queryable
          .of(() -> 
              { 
                System.out.println("supplying"); 
                return UUID.randomUUID();
              }
             )
          .map(uuid -> 
              {
                 System.out.println("mapping"); 
                 return uuid.toString();
              }
             )
          .limit(3)
          .forEach(System.out::println);

    }
}