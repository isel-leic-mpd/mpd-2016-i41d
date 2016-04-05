import java.util.function.Function;

/**
 * @author Miguel Gamboa
 *         created on 04-03-2016
 */
 

public class App {
    public static void composeFunctionsStrings() {
        Function<String, String> upper = str -> str.toUpperCase();
        Function<String, String> replaceSpaces = str -> str.replace(' ', '-');
        Function<String, String> c = upper.andThen(replaceSpaces);
        
        String INPUT = "ola super isel";
        
        System.out.println(c.apply(INPUT));
        
        String r = upper.apply(INPUT);
        String r2 = replaceSpaces.apply(r);
        System.out.println(r2);
    }

    public static void main(String[] args) {
        Function<Integer, Integer> dup = nr -> nr * 2;
        Function<Integer, Integer> inc  = nr -> nr + 1;
        Function<Integer, Integer> dupAndInc1 = dup.andThen(inc);
        Function<Integer, Integer> dupAndInc2 = inc.compose(dup); // <=> dup.andThen(inc)
    }
}

